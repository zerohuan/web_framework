package com.yjh.search.site.crawler;

import com.yjh.search.site.index.HtmlIndexer;
import com.yjh.search.site.model.TLinkEntity;
import com.yjh.search.site.service.LinkService;
import com.yjh.search.site.util.FileAccessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class MainCrawler implements Runnable {
	private static Logger logger = LogManager.getLogger();
	
	private final ArrayList<String> toExtendsURL = new ArrayList<String>();
	private final Set<String> visitedURLList = new HashSet<String>();
	private final Set<String> themeSite = new HashSet<String>();

	@Inject
	private HtmlIndexer htmlIndexer;
	@Inject
	private CrawlerConfig crawlerConfig;

	private final AtomicBoolean isRunning = new AtomicBoolean(false);

	@Inject
	private LinkService linkService;


	public MainCrawler() {
	}

	private void init() {
		this.visitedURLList.clear();
		this.toExtendsURL.clear();
		//TODO 清空链接记录

		//检查download目录
		File downDir = new File(crawlerConfig.getFilePath());
		
		if(downDir.exists()) {//清除旧文件
			FileAccessor.deleteDir(downDir);
		}
		
		if(!downDir.exists()) {
			downDir.mkdirs();
		}
		//载入seed列表
		if(crawlerConfig.getSeedUrls() != null) {
			for(String s : crawlerConfig.getSeedUrls()) {
				//提取主机名
				String host = getSiteIndent(s);
				themeSite.add(host);
				toExtendsURL.add(s);
			}
		}
	}
	
	private String getSiteIndent(String seedUrl) {
		String result = "default";
		URL url = null;
		try {
			url = new URL(seedUrl);
			result = url.getHost().replace("www.", "");
		} catch (MalformedURLException e) {
			logger.error(e);
		}
		return result;
	}

	public LinkService getLinkService() {
		return linkService;
	}

	public void setLinkService(LinkService linkService) {
		this.linkService = linkService;
	}

	public void deleterExtendsURL() {
		String url = "";
		synchronized (toExtendsURL) {
			if(!toExtendsURL.isEmpty()) {
				//从扩张表里面提取看，是否有URL
				url = toExtendsURL.remove(0);
				//从扩张表里删除已经爬去过的URL
				toExtendsURL.remove(url);
				//添加，从扩张表里删除的URL
				visitedURLList.add(url);
			}
		}
		
		//提取URL的内容
		String content = downloadPageContent(url);
		try {
			logger.debug(url);
			//提取URL链接存储到扩张表里面
			retrieveLinks(content,new URL(url));
			String filename = System.currentTimeMillis() + ".html";
			String path = crawlerConfig.getFilePath() + filename;

			//插入link记录
			if(StringUtils.isNotBlank(content)) {
				FileAccessor.writeAll(path, content, "utf-8");
				TLinkEntity link = new TLinkEntity();
				
				link.setUrl(url);
				link.setPath(path);
				link.setDateCreated(Instant.now());
				link.setDateModified(Instant.now());
				link.setFilename(filename);
				
				linkService.save(link);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}			
		
	}


	private String downloadPageContent(String str) {
		try {
			BaseFetcher fetcher = new BaseFetcher();
			String response = fetcher.getText(str, "utf-8");
			
			return response;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	private void retrieveLinks(String pageContents,URL pageUrl) {		
	
			if(!"".equals(pageContents)&&pageContents!=null){	
			//	提取所有超链接的URL
				Pattern p = Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]",Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(pageContents);
				
				while (m.find()) {
					String link = m.group(1).trim();
					if (link.length() < 1) {
		                continue;
		            }
		            
		            // Skip links that are just page anchors.忽略锚
		            if (link.charAt(0) == '#') {
		                continue;
		            }
		            
		            // Skip mailto links.忽略mailto
		            if (link.contains("mailto:")) {
		                continue;
		            }
		            
		            // 忽略 JavaScript links.
		            if (link.toLowerCase().contains("javascript")) {
		                continue;
		            }
		            
		            if (!link.contains("://")) {
		                // Handle absolute URLs.处理绝对URL
		                if (link.charAt(0) == '/') {
		                    link = "http://" + pageUrl.getHost() + link;
		                    // Handle relative URLs.处理相对URL
		                } else {
		                    String file = pageUrl.getFile();
		                    if (file.indexOf('/') == -1) {
		                        link = "http://" + pageUrl.getHost() + "/" + link;
		                    } else {
		                        String path =
		                                file.substring(0, file.lastIndexOf('/') + 1);
		                        link = "http://" + pageUrl.getHost() + path + link;
		                    }
		                }
		            }
		            int index = link.indexOf('#');
		            if (index != -1) {
		                link = link.substring(0, index);
		            }
    
					if(!visitedURLList.contains(link) && !toExtendsURL.contains(link)){
						boolean isTheme = true;
						//检查页面是否包含指定主题
						if(StringUtils.isNotBlank(crawlerConfig.getKeys())) {
							String[] keyArray = crawlerConfig.getKeys().split(CrawlerConfig.KEY_SEPARATOR);
							isTheme = false;
							
							for(String key : keyArray) {
								if(pageContents.indexOf(key)!=-1){
									isTheme = true;
									break;
								}	
							}
						}
						if(link.equalsIgnoreCase("http://sex.guokr.com/")) {
							logger.debug("*******************");
							logger.debug(visitedURLList.contains(link));
							logger.debug("*******************");
						}
						//判断是否是该站点的
						if(isInTargetSites(link) && isTheme) {
							toExtendsURL.add(link);
						}
					}					
				}
			}
	}
	
	private boolean isInTargetSites(String url) {
		boolean flag = false;
		
		for(String s : themeSite) {
			if(url.contains(s))
				flag = true;
		}
		
		return flag;
	}

	@Override
	public void run(){	
		if(!isRunning.compareAndSet(false, true)) {
			init();
			this.deleterExtendsURL();
			final long startTime = getNowMinutes();
			while(toExtendsURL.size()>0){
				if(visitedURLList.size() >= crawlerConfig.getCount()) {
					break;
				}
				if(getNowMinutes() - startTime >= crawlerConfig.getMinutes()) {
					break;
				}
						/*if(toExtendsURL.size()>10000){
								return;
						}*/
				logger.info("toExtendsURL大小是"+toExtendsURL.size());
				deleterExtendsURL();
			}
			isRunning.set(false);
			try {
				htmlIndexer.createIndex();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
	}
	
	private Long getNowMinutes() {
		long ms = System.currentTimeMillis();
		return ms / 1000 / 60;
	}

	/**
	 * 输出抓取状态
	 * @throws Exception
	 */
	public void printArray() throws Exception{
		Iterator<String> to=toExtendsURL.iterator();
		StringBuilder sb = new StringBuilder();
		File fileE = new File(crawlerConfig.getExtendFile());
		if(!fileE.exists()) {
			fileE.getParentFile().mkdirs();
			fileE.createNewFile();
		}
		PrintWriter out = new PrintWriter(fileE, "utf-8");
		while(to.hasNext()){
			String url=to.next();

			try {
				sb.append(toExtendsURL.size()).append("扩张表的ＵＲＬ-").append(url).append("\r\n");
				
				out.append(sb.toString());
				sb.setLength(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}	
		out.close();

		File fileV = new File(crawlerConfig.getVisitedFile());
		PrintWriter outV = new PrintWriter(fileV, "utf-8");
		Iterator<String> visitor=visitedURLList.iterator();
		while(visitor.hasNext()){
			String url=visitor.next();

			try {
				sb.append(visitedURLList.size()).append("访问表的ＵＲＬ-").append(url).append("\r\n");

				outV.append(sb.toString());
				sb.setLength(0);
			} catch (Exception e) {
				logger.error(e);
			}

			logger.debug(visitedURLList.size() + "访问表的URL＝" + url);
		}
		outV.close();
	}

	public AtomicBoolean getIsRunning() {
		return isRunning;
	}

	public static void main(String args[]) throws MalformedURLException{
		//创建自己的网络蜘蛛
//		MainCrawler crawler=new MainCrawler();
//		//删除一个已经抓取过的URL
//		crawler.deleterExtendsURL();
//		//运行
//		crawler.run();
//		crawler.visitedURLList.add("http://www.guokr.com/about/");
	}
}

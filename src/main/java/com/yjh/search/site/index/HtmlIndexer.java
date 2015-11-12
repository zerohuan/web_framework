package com.yjh.search.site.index;

import com.yjh.base.security.MD5;
import com.yjh.search.site.model.TLinkEntity;
import com.yjh.search.site.model.TLinkEx;
import com.yjh.search.site.service.LinkService;
import com.yjh.search.site.util.CoreConstants;
import com.yjh.search.site.util.FileAccessor;
import com.yjh.search.site.util.LuceneUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class HtmlIndexer {
	private static Logger logger = LogManager.getLogger();
	private static final int threadCountIndex = 5;

	private File[] htmlFiles; //待索引的html文件集

	@Inject
	private LinkService linkService;
	@Inject
	private IndexerConfig config;
	
	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	
	private static Set<String> ContentMD5Set = new HashSet<String>();
	
	public HtmlIndexer() {
	}
	
	public List<TLinkEx> searchByKeyWords(String keys, int page, int limit) {
		List<TLinkEx> results = new ArrayList<>();
		try {
			Directory directory = LuceneUtils.openFSDirectory(config.getIndexPath());
			IndexReader reader = DirectoryReader.open(directory);

			ExecutorService pool = Executors.newFixedThreadPool(threadCountIndex);

			//创建搜索对象
			final IndexSearcher indexSearcher = new IndexSearcher(reader, pool);
			//创建查询分析器，对File属性title进行查询,采用的分析器是StandardAnalyzer
			QueryParser queryparser = new QueryParser("content",
					new StandardAnalyzer());
			//分析用户输入的字符串
			final Query query = queryparser.parse(keys);
			List<Future<TopDocs>> futures = new ArrayList<>(threadCountIndex);

			for (int i = 0; i < threadCountIndex; i++) {
				futures.add(pool.submit(() -> {
					TopDocs topDocs = null;
					topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
					ScoreDoc[] scores = topDocs.scoreDocs;
					int length = scores.length;
					if (length <= 0) {
						return null;
					}
					return topDocs;
				}));
			}
			int totalCount = 0;
			for(Future<TopDocs> future : futures) {
				TopDocs topDocs = future.get();
				if(topDocs != null) {
					totalCount += topDocs.totalHits;
				}
			}
			CoreConstants.PageInfo pi = CoreConstants.getStartIndex(page, limit, totalCount);

			int itemCount = pi.getStart();
			for (int i = 0; i < futures.size(); i++) {
				Future<TopDocs> future = futures.get(i);
				TopDocs topDocs = future.get();
				if(topDocs != null) {
					for(ScoreDoc scoreDoc : topDocs.scoreDocs) {
						if(++itemCount > pi.getEnd()) {
							break;
						}
						Document doc = indexSearcher.doc(scoreDoc.doc);
						TLinkEx article = new TLinkEx();

						article.setTitle(doc.get("title"));
						article.setContent(getSummary(doc.get("content"), keys));
						article.setDateCreated(Instant.parse(doc.get("time")));
						article.setUrl(doc.get("url"));
						article.setCount(totalCount);
						//把document添加到集合中,并且返回
						results.add(article);
					}
				}
			}
			//释放线程池资源
			pool.shutdown();

		} catch (Exception e) {
			logger.error(e);
		}
		return results;
	}
	
	private static String getSummary(String content, String keys) {
		StringBuilder sb = new StringBuilder();
		org.jsoup.nodes.Document doc = Jsoup.parse(content);
		
		String result = doc.text();
		int len = result.length();
		String[] keysArr = keys.split(" ");
		//组成正则表达式
		String headS = "";
		for(String s : keysArr) {
			sb.append(headS).append(s);
			headS = "|";
		}
		String patternKeys = sb.toString();
		sb.setLength(0);
		Pattern kp;
		Matcher km;
		if(StringUtils.isNotBlank(patternKeys)) {
			kp = Pattern.compile(patternKeys);
			km = kp.matcher(result);
			
			while(km.find()) {
				int start = km.start();
				int end = km.end();
				sb.append(result.substring(start > 15 ? start - 15 : start, end < len - 15 ? end + 15 : end)).append("......");
			}
		}
		
		String temp = result;
		result = sb.toString();
		
		len = result.length();
		
		if(len < 50) {
			int tlen = temp.length();
			if(tlen > 0)
				result += temp.substring(tlen / 2, tlen);
		}
		len = result.length();
		result = result.substring(0, 200 > len ? len : 200);
		
		if(StringUtils.isNotBlank(patternKeys)) {
			result = result.replaceAll(patternKeys, "[:red$0red:]");
		}

		return result;
	}
	
	/**
	 * 创建索引
	 * @throws Exception
	 */
	public void createIndex() throws Exception {
		final Analyzer luceneAnalyzer = new StandardAnalyzer();
		Directory indexDir = LuceneUtils.openFSDirectory(config.getIndexPath());
		final IndexWriter indexWriter = new IndexWriter(indexDir, new IndexWriterConfig(luceneAnalyzer));
		if(!isRunning.compareAndSet(false, true)) {
			new Thread(()->{
				try {
					File htmlDir = new File(config.getPath());
					if(htmlDir.exists() && htmlDir.isDirectory()) {
						htmlFiles = htmlDir.listFiles();
					}
					//Add documents to the index
					if(htmlFiles != null) {
						for(int i = 0; i < htmlFiles.length; i++){
							logger.debug("no" + i + "\t");
							if(htmlFiles[i].isFile() && htmlFiles[i].getName().endsWith(".html")){
								logger.debug("File " + htmlFiles[i].getCanonicalPath()
										+ " is being indexed");
								String content = FileAccessor.readAll(htmlFiles[i], "utf-8");
								if(StringUtils.isNotBlank(content)) {
									String contentMd5 = MD5.md5(content);

									if(ContentMD5Set.contains(contentMd5)) {
										logger.debug("same!!!!!");
										continue;
									}
									String filename = htmlFiles[i].getName();
									List<TLinkEntity> links = linkService.findByFilename(filename);
									if(links.size() > 0) {
										TLinkEntity link = links.get(0);

										org.jsoup.nodes.Document doc = Jsoup.parse(content);
										String title = doc.select("title").text();

										Document document = new Document();

										document.add(new TextField("content",content,
												Field.Store.YES));
										document.add(new TextField("path",htmlFiles[i].getPath(),
												Field.Store.YES));
										document.add(new TextField("url", link.getUrl(),
												Field.Store.YES));
										document.add(new TextField("time", link.getDateCreated().toString(),
												Field.Store.YES));
										if(StringUtils.isNotBlank(title)) {
											document.add(new TextField("title",title,
													Field.Store.YES));
											indexWriter.addDocument(document); //只有标题不为空才加入索引
											ContentMD5Set.add(contentMd5);
										}
									}
								}
							}
						}
					}
				} catch(Exception e) {
					logger.error(e);
				} finally {
					try {
						indexWriter.close();
					} catch (Exception e) {
						logger.error(e);
					}
					isRunning.set(false);
					logger.info("indexing end!--------------------");
				}
			}).start();
		}

	}
}

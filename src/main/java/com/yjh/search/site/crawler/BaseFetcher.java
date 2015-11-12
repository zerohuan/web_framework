package com.yjh.search.site.crawler;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLHandshakeException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ConnectException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 封装一些基本的抓取的方法
 * @author yjh
 *
 */
public class BaseFetcher implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8559218657891040653L;
	
//	private static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
	private DefaultHttpClientSerial client;
	private String loginURL = "";
	private String baseURL = "";
	private List<NameValuePair> nvps; //排除不需要序列化存储的部分
	private transient HttpResponse response; 
	private String cookie;
	private String location;
	private String host;
	private transient VerfierProxy verfier;
	private Map<String, String> httpHeader;
	private boolean isLogin = false;
	public Map<String, String> getHttpHeader() {
		return httpHeader;
	}

	public void setHttpHeader(Map<String, String> httpHeader) {
		this.httpHeader = httpHeader;
	}

    /**
     * 构造函数
     */
	public BaseFetcher() {
		
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
	    HttpConnectionParams.setSoTimeout(params, 20 * 1000);
//		client = new DefaultHttpClientSerial(params);
	    client = new DefaultHttpClientSerial();
		client.setParams(params);
		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int excutionCount, HttpContext context) {
				if (excutionCount >= 3) {
					//如果超过最大重连次数，那么久不要继续了
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					//如果服务器丢掉了连接，那么就重连
					return true;
				}
				if (exception instanceof SSLHandshakeException) {
					// 不要重试SSL握手异常
					return false;
				}
				
				HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					//如果请求被认为是幂等的，那么就重试
					return true;
				}
				return false;
			}
		};
		
		client.setHttpRequestRetryHandler(retryHandler);
	}
	
	
	
	public BaseFetcher(String loginURL, List<NameValuePair> nvps, VerfierProxy verfier) {
		this();
		this.loginURL = loginURL;
		this.nvps = nvps;
		this.verfier = verfier;
	}
	
	
	
	public BaseFetcher(String loginURL, List<NameValuePair> nvps,
			VerfierProxy verfier, Map<String, String> httpHeader) {
		this();
		this.loginURL = loginURL;
		this.nvps = nvps;
		this.verfier = verfier;
		this.httpHeader = httpHeader;
	}



	/**
	 * 登录相应的系统，并借助对应的校验器确定是否登录成功
	 * @param encoding
	 * @return
	 * @throws ConnectException 
	 */
	public boolean login(String encoding) throws ConnectException {
		HttpPost postMethod = new HttpPost(loginURL);
		try {
//			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			postMethod.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
			postMethod.addHeader("Cookie", cookie);
			postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);
			postMethod.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
			postMethod.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			if(httpHeader != null) {
				for(Map.Entry<String, String> entry : httpHeader.entrySet()) {
					//System.out.println(entry.getValue());
					postMethod.setHeader(entry.getKey(), entry.getValue());
				}
			}
			response = client.execute(postMethod);
			StringBuilder sb = new StringBuilder();
			Header[] headers = response.getHeaders("Set-Cookie");
			if(!StringUtils.isBlank(cookie))
				sb.append(cookie).append(" ");
			for(Header header: headers){
				sb.append(header.getValue());
			}
			if(!sb.toString().equals("") && sb != null){
				cookie = sb.toString();
			}
//			System.out.println("cookie:" + cookie);
			location = response.getFirstHeader("Location") != null ? response.getFirstHeader("Location").getValue() : "";
//			System.out.println("location:" + location);
			if(!StringUtils.isBlank(location) && !location.startsWith("http")) {
				location = this.getBaseURL() + location;
			}
			return verfier.loginVerify(this);
		} catch(ConnectException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			postMethod.abort();
		}
	}
	
	/**
	 * 登录相应的系统，并借助对应的校验器确定是否登录成功
	 * @param encoding
	 * @return
	 */
	public boolean loginWithEncoding(String encoding) {
		HttpPost postMethod = new HttpPost(loginURL);
		try {
			postMethod.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
			postMethod.addHeader("Cookie", cookie);
			postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);
			postMethod.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
			postMethod.setEntity(new UrlEncodedFormEntity(nvps, encoding));
			if(httpHeader != null) {
				for(Map.Entry<String, String> entry : httpHeader.entrySet()) {
					//System.out.println(entry.getValue());
					postMethod.setHeader(entry.getKey(), entry.getValue());
				}
			}
			response = client.execute(postMethod);
			StringBuilder sb = new StringBuilder();
			Header[] headers = response.getHeaders("Set-Cookie");
			if(!StringUtils.isBlank(cookie))
				sb.append(cookie).append(" ");
			for(Header header: headers){
				sb.append(header.getValue());
			}
			if(!sb.toString().equals("") && sb != null){
				cookie = sb.toString();
			}
//			System.out.println("cookie:" + cookie);
			location = response.getFirstHeader("Location") != null ? response.getFirstHeader("Location").getValue() : "";
//			System.out.println("location:" + location);
			if(!StringUtils.isBlank(location) && !location.startsWith("http")) {
				location = this.getBaseURL() + location;
			}
			return verfier.loginVerify(this);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			postMethod.abort();
		}
	}
	

	
	/**
	 * 用get方法访问验证码的地址，并将验证码图片转存到服务器
	 * @param url
	 * @param file
	 * @return
	 */
	public boolean transVerifyImg(String url, File file){
		HttpGet getMethod = new HttpGet(url);
		try {
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			// 执行getMethod
			response = client.execute(getMethod);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return false;
			}
			StringBuilder sb = new StringBuilder();
			Header[] headers = response.getHeaders("Set-Cookie");
			for(Header header: headers){
				sb.append(header.getValue());
			}
			if(sb != null && !sb.toString().equals(""))
			    cookie = sb.toString();
			// 读取内容
			InputStream inputStream = response.getEntity().getContent();
//			byte[] bytes = new byte[inputStream.available()];
//			inputStream.read(bytes);
			OutputStream outStream = new FileOutputStream(file);
			IOUtils.copy(inputStream, outStream);
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.abort();
		}
		return true;
	}
	
	public byte[] transVerifyImgToArrayCookie(String url, File file){
		HttpGet getMethod = new HttpGet(url);
		byte[] bytes = null;
		try {
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			response = client.execute(getMethod);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			Header[] headers = response.getHeaders("Set-Cookie");
			for(Header header: headers){
				sb.append(header.getValue());
			}
			if(sb != null && !sb.toString().equals(""))
			    cookie = sb.toString();
			InputStream inputStream = response.getEntity().getContent();
//			bytes = new byte[inputStream.available()];
//			inputStream.read(bytes);
			OutputStream outStream = new FileOutputStream(file);
			IOUtils.copy(inputStream, outStream);
			outStream.close();
			inputStream.close();
			inputStream = new FileInputStream(file);
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getMethod.abort();
		}
		return bytes;
	}
	
	/**
	 * 将制定url的图片转成二进制返回
	 * @param url
	 * @param file
	 * @return
	 */
	public byte[] transVerifyImgToArray(String url, File file){
		HttpGet getMethod = new HttpGet(url);
		byte[] bytes = null;
		try {
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			response = client.execute(getMethod);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			Header[] headers = response.getHeaders("Set-Cookie");
			//if(!StringUtils.isBlank(cookie))
				//sb.append(cookie).append("; ");
			for(Header header: headers){
				sb.append(header.getValue());
			}
//			if(sb != null && !sb.toString().equals(""))
//			    cookie = sb.toString();
			InputStream inputStream = response.getEntity().getContent();
			OutputStream outStream = new FileOutputStream(file);
			IOUtils.copy(inputStream, outStream);
			outStream.close();
			inputStream.close();
			inputStream = new FileInputStream(file);
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getMethod.abort();
		}
		return bytes;
	}
	
	/**
	 * 将制定url的图片转成二进制返回
	 * @param url
	 * @param file
	 * @return
	 */
	public byte[] transVerifyImgToArray(String url, File file, Map<String, String> headMap){
		HttpGet getMethod = new HttpGet(url);
		byte[] bytes = null;
		try {
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			if(headMap != null) {
				for(Map.Entry<String, String> entry : headMap.entrySet()) {
					getMethod.setHeader(entry.getKey(), entry.getValue());
				}
			}
			response = client.execute(getMethod);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			Header[] headers = response.getHeaders("Set-Cookie");
			//if(!StringUtils.isBlank(cookie))
				//sb.append(cookie).append("; ");
			for(Header header: headers){
				sb.append(header.getValue());
			}
//			if(sb != null && !sb.toString().equals(""))
//			    cookie = sb.toString();
			InputStream inputStream = response.getEntity().getContent();
//			System.out.println(inputStream.available());
			OutputStream outStream = new FileOutputStream(file);
			IOUtils.copy(inputStream, outStream);
			outStream.close();
			inputStream.close();
			inputStream = new FileInputStream(file); //再次从文件见中读
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getMethod.abort();
		}
		return bytes;
	}
	
	/**
	 * 将制定url的图片转成二进制返回，for矿大
	 * @param url
	 * @param file
	 * @return
	 */
	public byte[] transVerifyImgToArrayT(String url, File file, Map<String, String> headMap){
		HttpGet getMethod = new HttpGet(url);
		byte[] bytes = null;
		try {
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			if(headMap != null) {
				for(Map.Entry<String, String> entry : headMap.entrySet()) {
					getMethod.setHeader(entry.getKey(), entry.getValue());
				}
			}
			response = client.execute(getMethod);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			Header[] headers = response.getHeaders("Set-Cookie");
			for(Header header: headers){
				sb.append(header.getValue());
			}
			if(sb.length() > 0) {
				cookie = sb.toString();
			}
			InputStream inputStream = response.getEntity().getContent();
			BufferedImage image = ImageIO.read(inputStream);     //将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
			ImageIO.write(image, "gif", file); //自己看
			inputStream.close();
			inputStream = new FileInputStream(file); //再次从文件见中读
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getMethod.abort();
		}
		return bytes;
	}
	
	public String getTextURL(String url, String encoding, Map<String , String> headers){
		URI uri = null;
		try {
			URL urlN  = new URL(url);
			uri  = new URI(urlN.getProtocol(), urlN.getHost(), urlN.getPath(), urlN.getQuery(), null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpGet getMethod = new HttpGet(uri);
		getMethod.addHeader("Cookie", cookie);
		getMethod.addHeader("Content-Type", "text/html; charset=" + encoding);
		if(headers != null) {
			for(Map.Entry<String, String> entry : headers.entrySet()) {
				getMethod.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// Create a response handler
		//ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			response = client.execute(getMethod);
			HttpEntity entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, encoding);
		}catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			getMethod.abort();
		}
		return responseBody;
	}
	
	/**
	 * 用get方法获得页面的内容
	 * @param url
	 * @param encoding
	 * @return
	 */
	public String getText(String url, String encoding){
		HttpGet getMethod = new HttpGet(url);
		getMethod.addHeader("Cookie", cookie);
		getMethod.addHeader("Content-Type", "text/html; charset=" + encoding);
		// Create a response handler
		//ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			response = client.execute(getMethod);
			HttpEntity entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, encoding);
		}catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			getMethod.abort();
		}
		return responseBody;
	}
	
	/**
	 * 用get方法获得页面的内容
	 * @param url
	 * @param encoding
	 * @return
	 */
	public String getText(String url, String encoding, Map<String , String> headers) {
		HttpGet getMethod = new HttpGet(url);
		getMethod.addHeader("Cookie", cookie);
		getMethod.addHeader("Content-Type", "text/html; charset=" + encoding);
		if(headers != null) {
			for(Map.Entry<String, String> entry : headers.entrySet()) {
				getMethod.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// Create a response handler
		//ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			response = client.execute(getMethod);
			HttpEntity entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, encoding);
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			getMethod.abort();
			
		}
		return responseBody;
	}
	
	public byte[] getTextByteArray(String url, String encoding, Map<String , String> headers) {
		HttpGet getMethod = new HttpGet(url);
		getMethod.addHeader("Cookie", cookie);
		getMethod.addHeader("Content-Type", "text/html; charset=" + encoding);
		if(headers != null) {
			for(Map.Entry<String, String> entry : headers.entrySet()) {
				getMethod.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// Create a response handler
		//ResponseHandler<String> responseHandler = new BasicResponseHandler();
		byte[] bytearray = null;
		try {
			response = client.execute(getMethod);
			HttpEntity entity = response.getEntity();
			bytearray = EntityUtils.toByteArray(entity);
		} catch (Exception e) {
			e.printStackTrace();
			bytearray = null;
		} finally {
			getMethod.abort();
			
		}
		return bytearray;
	}
	
	/**
	 * 用get方法获得页面的内容	yjh	2014-7-5
	 * @param url
	 * @param encoding
	 * @return
	 */
	public String getResponseLocation(String url, String encoding) {
		HttpGet getMethod = new HttpGet(url);
		//首先要是的httpclient不要自动跳转（在Get方法中）
		HttpParams params = new BasicHttpParams();
		params.setParameter("http.protocol.handle-redirects", false);
		getMethod.setParams(params);
		getMethod.addHeader("Cookie", cookie);
		getMethod.addHeader("Content-Type", "text/html; charset=" + encoding);
		getMethod.addHeader("Connection", "keep-alive");
		getMethod.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0");
		// Create a response handler
		//ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String locationURL = "";
		String responseBody = "";
		try {
			response = client.execute(getMethod);
			HttpEntity entity = response.getEntity();			
			responseBody = EntityUtils.toString(entity, encoding);
			locationURL = response.getFirstHeader("Location") != null ? response.getFirstHeader("Location").getValue() : "";
			if(locationURL == null || locationURL.isEmpty()) {
				locationURL = url;
			} else {
				if(!locationURL.startsWith("http")) {
					locationURL = this.baseURL + locationURL;
				}
			}
			location = locationURL;
		} catch (Exception e) {
			e.printStackTrace();
			location = url;
			responseBody = null;
		} finally {
			getMethod.abort();
			
		}
		return responseBody;
	}
	
	/**
	 * 用get方法获得页面的内容	同时设置location，如果有cookies则设置cookies
	 * @param url
	 * @param encoding
	 * @return
	 */
	public String getResponseLocationCookie(String url, String encoding) {
		HttpGet getMethod = new HttpGet(url);
		//首先要是的httpclient不要自动跳转（在Get方法中）
		HttpParams params = new BasicHttpParams();
		params.setParameter("http.protocol.handle-redirects", false);
		getMethod.setParams(params);
		getMethod.addHeader("Cookie", cookie);
		getMethod.addHeader("Content-Type", "text/html; charset=" + encoding);
		getMethod.addHeader("Connection", "keep-alive");
		getMethod.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0");
		// Create a response handler
		//ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String locationURL = "";
		String responseBody = "";
		try {
			response = client.execute(getMethod);
			HttpEntity entity = response.getEntity();
			
			
			Header[] headers = response.getHeaders("Set-Cookie");
			StringBuilder sb = new StringBuilder();
//			if(!StringUtils.isBlank(cookie))
//				sb.append(cookie).append(" ");
			for(Header header: headers){
				sb.append(header.getValue());
			}
			if(sb.length()>0)
     			cookie = sb.toString();
			
			
			responseBody = EntityUtils.toString(entity, encoding);
			locationURL = response.getFirstHeader("Location") != null ? response.getFirstHeader("Location").getValue() : "";
			if(locationURL == null || locationURL.isEmpty()) {
				locationURL = url;
			} else {
				if(!locationURL.startsWith("http")) {
					locationURL = this.baseURL + locationURL;
				}
			}
			location = locationURL;
		} catch (Exception e) {
			e.printStackTrace();
			location = url;
			responseBody = null;
		} finally {
			getMethod.abort();
			
		}
		return responseBody;
	}
	
	/**
	 * 用get方法获得页面的内容并设置cookie
	 * @param url
	 * @param encoding
	 * @return
	 */
	public String getTextSetCookie(String url, String encoding) {
		HttpGet getMethod = new HttpGet(url);
		getMethod.addHeader("Cookie", cookie);
		// Create a response handler
		//ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			response = client.execute(getMethod);
			HttpEntity entity = response.getEntity();
			StringBuilder sb = new StringBuilder();
			Header[] headers = response.getHeaders("Set-Cookie");
//			if(!StringUtils.isBlank(cookie))
//				sb.append(cookie).append(" ");
			for(Header header: headers){
				sb.append(header.getValue());
			}
			cookie = sb.toString();
			responseBody = EntityUtils.toString(entity, encoding);
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			getMethod.abort();
			
		}
		return responseBody;
	}
	
	/**
	 * 带sso单点访问控制的页面进行抓取
	 * @param url
	 * @return
	 */
	public String getTextSSO(String url, String ssoUrl, String encoding) {
		Random r = new Random();
		String sso = ssoUrl + String.valueOf(r.nextDouble());
		HttpGet getMethod = new HttpGet(sso);
		getMethod.addHeader("Cookie", cookie);
		getMethod.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.76 Safari/537.36");
		getMethod.addHeader("Content-Type", "text/html; charset=" + encoding);
		String responseBody = "";
		try {
			response = client.execute(getMethod);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < response.getHeaders("Set-Cookie").length; i ++) {
				if (i != 0) {
					sb.append("," + response.getHeaders("Set-Cookie")[i].getValue() );
				}
			}
			cookie = sb.toString();
			HttpEntity entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, encoding);
			getMethod = new HttpGet(url);
			getMethod.addHeader("Cookie", cookie);
			getMethod.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.76 Safari/537.36");
			getMethod.addHeader("Content-Type", "text/html; charset=" + encoding);
			
			response = client.execute(getMethod);
			entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, encoding);
			if(responseBody.isEmpty())
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			getMethod.abort();
			
		}
		return responseBody;
	}
	
	/**
	 * post获取webservice接口的数据
	 * 
	 * @param url
	 * @param postEntity
	 * @param encoding
	 * @return
	 */
	public String getPostWebService(String url, String postEntity,
			String encoding) {
		HttpPost postMethod = new HttpPost(url);
		String responseBody = "";
		try {
			postMethod.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS,
					false);
			HttpEntity re = new StringEntity(postEntity, HTTP.UTF_8);
			postMethod.setEntity(re);
			postMethod.setHeader("Host", "ykt.njust.edu.cn");
			postMethod.setHeader("Content-Type",
					"application/soap+xml; charset=" + encoding);
			response = client.execute(postMethod);
			HttpEntity entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			//将返回值设为空以提供未能正常获得信息的判断
			responseBody = null;
		} finally {
			postMethod.abort();
		}
		return responseBody;
	}

	/**
	 * 根据cookies获取post内容
	 * @param url
	 * @param nvps
	 * @param encoding
	 * @return
	 */
	public String getPostContentWithCookie(String url,
			List<NameValuePair> nvps, String encoding) {
		HttpPost postMethod = new HttpPost(url);
		postMethod.addHeader("Cookie", cookie);
		String responseBody = "";
		try {
			postMethod.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS,
					false);
			postMethod.setHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=" + encoding);
			postMethod.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			UrlEncodedFormEntity test = new UrlEncodedFormEntity(nvps,
					HTTP.UTF_8);
			response = client.execute(postMethod);
			cookie = response.getFirstHeader("Set-Cookie").getValue();
			HttpEntity entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, encoding);
		} catch (Exception e) {
			e.printStackTrace();
			// 将返回值设为空以提供未能正常获得信息的判断
			responseBody = null;
		} finally {
			postMethod.abort();
		}
		return responseBody;
	}

	/**
	 * 使用post方法获取指定url的响应
	 * @param url
	 * @param nvps
	 * @param encoding
	 * @return
	 */
	public String getPostContent(String url, List<NameValuePair> nvps, String encoding) {
		HttpPost postMethod = new HttpPost(url);
		postMethod.addHeader("Cookie", cookie);
		String responseBody = "";
		try {
			postMethod.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
			postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);
			postMethod.setEntity(new UrlEncodedFormEntity(nvps , HTTP.UTF_8));
			response = client.execute(postMethod);
			HttpEntity entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, encoding);
			location = response.getFirstHeader("Location") != null ? response.getFirstHeader("Location").getValue() : "";
			//System.out.println("location:" + location);
			//重新载入location获得响应
			if(!location.isEmpty()) {
				if(!location.startsWith("http")) {
					responseBody = this.getText(baseURL + location, encoding);
					//System.out.println(baseURL + location);
				} else 
					responseBody = this.getText(location, encoding);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//将返回值设为空以提供未能正常获得信息的判断
			responseBody = null;
		} finally {
			postMethod.abort();
		}
		return responseBody;
	}
	
	/**
	 * 使用post方法获取指定url的响应
	 * @param url
	 * @param nvps
	 * @param encoding
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public byte[] getPostContentByteArray(String url, List<NameValuePair> nvps, String encoding) {
		HttpPost postMethod = new HttpPost(url);
		postMethod.addHeader("Cookie", cookie);
		byte[] bytearray = null;
		try {
			postMethod.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
			postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);
			postMethod.setEntity(new UrlEncodedFormEntity(nvps , HTTP.UTF_8));
			response = client.execute(postMethod);
			HttpEntity entity = response.getEntity();
			bytearray = EntityUtils.toByteArray(entity);
			location = response.getFirstHeader("Location") != null ? response.getFirstHeader("Location").getValue() : "";
			//System.out.println("location:" + location);
			//重新载入location获得响应
			if(!location.isEmpty()) {
				if(!location.startsWith("http")) {
					bytearray = this.getTextByteArray(baseURL + location, encoding, null);
					//System.out.println(baseURL + location);
				} else 
					bytearray = this.getTextByteArray(location, encoding, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			bytearray = null;
		} finally {
			postMethod.abort();
		}
		return bytearray;
	}
	
	/**
	 * 使用post方法获取指定url的响应
	 * @param url
	 * @param nvps
	 * @param encoding
	 * @return
	 */
	public String getPostContent(String url, List<NameValuePair> nvps, String encoding, String requsetEncoding) {
		HttpPost postMethod = new HttpPost(url);
		postMethod.addHeader("Cookie", cookie);
		String responseBody = "";
		try {
			postMethod.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
			postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);
			postMethod.setEntity(new UrlEncodedFormEntity(nvps , requsetEncoding));
			response = client.execute(postMethod);
			HttpEntity entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, encoding);
			location = response.getFirstHeader("Location") != null ? response.getFirstHeader("Location").getValue() : "";
			//System.out.println("location:" + location);
			//重新载入location获得响应
			if(!location.isEmpty()) {
				if(!location.startsWith("http")) {
					responseBody = this.getText(baseURL + location, encoding);
					//System.out.println(baseURL + location);
				} else 
					responseBody = this.getText(location, encoding);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//将返回值设为空以提供未能正常获得信息的判断
			responseBody = null;
		} finally {
			postMethod.abort();
		}
		return responseBody;
	}
	
	/**
	 * 此为带额外head的版本
	 * @param url
	 * @param nvps
	 * @param encoding
	 * @param headMap
	 * @return
	 */
	public String getPostContent(String url, List<NameValuePair> nvps, String encoding, Map<String, String> headMap) {
		HttpPost postMethod = new HttpPost(url);
		postMethod.addHeader("Cookie", cookie);
		String responseBody = "";
		try {
			postMethod.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
			postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);
			postMethod.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			if(headMap != null) {
				for(Map.Entry<String, String> entry : headMap.entrySet()) {
					postMethod.setHeader(entry.getKey(), entry.getValue());
				}
			}
			for(Map.Entry<String, String> entry : headMap.entrySet()) {
				postMethod.setHeader(entry.getKey(), entry.getValue());
			}
			response = client.execute(postMethod);
			HttpEntity entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, encoding);
//			System.out.println(Arrays.toString(response.getAllHeaders()));
			location = response.getFirstHeader("Location") != null ? response.getFirstHeader("Location").getValue() : "";
		} catch (Exception e) {
			e.printStackTrace();
			//将返回值设为空以提供未能正常获得信息的判断
			responseBody = null;
		} finally {
			postMethod.abort();
		}
		return responseBody;
	}

	/**
	 * 查询南大一卡通详情，这个较为特殊，因此单独写一个方法
	 * @param str
	 * @param beginDate
	 * @param endDate
	 * @param page
	 * @param username
	 * @return
	 */
	public String queryDetail(String str, String beginDate, String endDate, String page, String username) {
		Document doc = Jsoup.parse(str);
		Elements items = doc.select("form[name=TradeDetailQueryForm]");
		String posturl = "";
		for(int i = 0; i < items.size(); i ++) {
			posturl = items.get(i).attr("action");
		}
		String token = "";
		items = doc.select("input[name=org.apache.struts.taglib.html.TOKEN]");
		for(int i = 0; i < items.size(); i ++) {
			token = items.get(i).attr("value");
		}
		String cardId = "";
		items = doc.select("select[name=cardId] option");
		for(int i = 0; i < items.size(); i ++) {
			cardId = items.get(i).attr("value");
			break;
		}
		String begindate = beginDate;
		String enddate = endDate;
		String serialType = "1";
		HttpPost postMethod = new HttpPost(posturl);
		try {
			StringEntity e = new StringEntity("page="+page + "&org.apache.struts.taglib.html.TOKEN=" + token + "&beginDate=" + begindate + "&endDate=" + enddate + "&cardId="+cardId+"&serialType="+serialType);
			e.setContentType("application/x-www-form-urlencoded");
			postMethod.addHeader("Referer", "http://ecard.nju.edu.cn/web/"+username+"/6");
			postMethod.addHeader("KeepAlive", "true");
			postMethod.addHeader("Cookie", cookie);
			postMethod.addHeader("Host", "ecard.nju.edu.cn");
			postMethod.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
			postMethod.setEntity(e);
			response = client.execute(postMethod);
			if(response != null) {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, "UTF-8");
				return result;
			} else {
				return "";
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} finally {
			postMethod.abort();
		}

	}
	/**
	 * 释放连接
	 */
	public void close(){
		client.getConnectionManager().shutdown();
	}
	
	public String getLoginURL() {
		return loginURL;
	}



	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}



	public String getLocation() {
		return location;
	}



	public void setLocation(String location) {
		this.location = location;
	}



	public VerfierProxy getVerfier() {
		return verfier;
	}



	public void setVerfier(VerfierProxy verfier) {
		this.verfier = verfier;
	}



	public DefaultHttpClient getClient() {
		return client;
	}



	public void setClient(DefaultHttpClientSerial client) {
		this.client = client;
	}



	public List<NameValuePair> getNvps() {
		return nvps;
	}



	public void setNvps(List<NameValuePair> nvps) {
		this.nvps = nvps;
	}



	public HttpResponse getResponse() {
		return response;
	}



	public void setResponse(HttpResponse response) {
		this.response = response;
	}



	public String getCookie() {
		return cookie;
	}



	public void setCookie(String cookie) {
		this.cookie = cookie;
	}	
	
	public void setParams(String loginURL, List<NameValuePair> nvps, VerfierProxy verfier) {
		this.loginURL = loginURL;
		this.nvps = nvps;
		this.verfier = verfier;
	}
	
	public void setParams(String loginURL, List<NameValuePair> nvps,
			VerfierProxy verfier, Map<String, String> httpHeader) {
		this.loginURL = loginURL;
		this.nvps = nvps;
		this.verfier = verfier;
		this.httpHeader = httpHeader;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
	/*****************************************************规范的新方法*******************************************************/
}


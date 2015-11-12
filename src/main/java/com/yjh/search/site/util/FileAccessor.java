package com.yjh.search.site.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 文件操作类	2014-7-18
 * @author yjh
 *
 */
public class FileAccessor {
	/**
	 * 读取所有内容，文件不存在返回null
	 * @param filename
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String readAll(String filename, String encoding) throws Exception {
		StringBuilder sb = new StringBuilder();
		File file = new File(filename);
		if(file.exists()) {
			char[] buf = new char[1024];
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
			int len = 0;
			while((len = br.read(buf)) != -1) {
				sb.append(buf, 0, len);//不要忘了每次都要读取len，否则最后会有问题
			}
			br.close();
		} else {
			return null;
		}
		return sb.toString();
	}
	
	/**
	 * 当文件不存在时返回null
	 * @param file
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String readAll(File file, String encoding) throws Exception {
		StringBuilder sb = new StringBuilder();
		if(file.exists()) {
			char[] buf = new char[1024];
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
			int len = 0;
			while((len = br.read(buf)) != -1) {
				sb.append(buf, 0, len);//不要忘了每次都要读取len，否则最后会有问题
			}
			br.close();
		} else {
			return null;
		}
		return sb.toString();
	}
	
	/**
	 * 写入所有内容
	 * @param filename
	 * @param content
	 * @param encoding
	 * @throws Exception
	 */
	public static void writeAll(String filename, String content, String encoding) throws Exception {
		File file = new File(filename);
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		PrintWriter out = new PrintWriter(file, encoding);
		out.write(content);
		out.flush();
		out.close();
	}
	
	 public static boolean deleteDir(File dir) {
	        if (dir.isDirectory()) {
	            String[] children = dir.list();
	            //递归删除目录中的子目录下
	            for (int i=0; i<children.length; i++) {
	                boolean success = deleteDir(new File(dir, children[i]));
	                if (!success) {
	                    return false;
	                }
	            }
	        }
	        // 目录此时为空，可以删除
	        return dir.delete();
	    }
}


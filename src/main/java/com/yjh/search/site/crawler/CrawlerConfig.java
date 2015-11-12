package com.yjh.search.site.crawler;

/**
 * Configuration of crawler
 * It's a final class
 *
 * Created by yjh on 15-11-5.
 */
public final class CrawlerConfig {
    public static final String KEY_SEPARATOR = " ";
    //抓取文件的保存地址
    private String filePath = "";
    private String extendFile;
    private String visitedFile;
    //爬虫本次运行的最长时间
    private Long minutes = 0L;
    //爬虫本次抓取的数量限制
    private long count = 0;
    //抓取关键字
    private String keys;
    private String[] seedUrls;

    public CrawlerConfig() {
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExtendFile() {
        return extendFile;
    }

    public void setExtendFile(String extendFile) {
        this.extendFile = extendFile;
    }

    public String getVisitedFile() {
        return visitedFile;
    }

    public void setVisitedFile(String visitedFile) {
        this.visitedFile = visitedFile;
    }

    public String[] getSeedUrls() {
        return seedUrls;
    }

    public void setSeedUrls(String[] seedUrls) {
        this.seedUrls = seedUrls;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }
}

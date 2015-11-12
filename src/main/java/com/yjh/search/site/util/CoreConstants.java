package com.yjh.search.site.util;

/**
 * Global static final config
 *
 * Created by yjh on 15-11-5.
 */
public class CoreConstants {
    public static final String INDEX_FILE_PATH = "/home/yjh/data/search_engine";
    public static final String CRAWLER_INDEX_DIR = INDEX_FILE_PATH + "/index";

    public static PageInfo getStartIndex(int page, int limit, int size) {
        int start = (page - 1) * limit;
        int end = start + limit;

        if(start > size) {
            start = size;
            end = size;
        } else if(end > size) {
            end = size;
        }

        return new PageInfo(start, end);
    }

    public static class PageInfo {
        private int start;
        private int end;

        public PageInfo(int start, int end) {
            super();
            this.start = start;
            this.end = end;
        }
        public int getStart() {
            return start;
        }
        public void setStart(int start) {
            this.start = start;
        }
        public int getEnd() {
            return end;
        }
        public void setEnd(int end) {
            this.end = end;
        }
    }
}

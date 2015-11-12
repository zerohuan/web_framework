package com.yjh.search.site.model;

/**
 * Created by yjh on 15-11-6.
 */
public class TLinkEx extends TLinkEntity {
    private String content;
    private String title;
    private int count;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

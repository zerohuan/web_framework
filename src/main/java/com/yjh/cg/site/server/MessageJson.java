package com.yjh.cg.site.server;

import java.io.Serializable;

/**
 * Created by yjh on 15-10-16.
 */
public class MessageJson implements Serializable {
    private long replyUserId;
    private String message;

    public long getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

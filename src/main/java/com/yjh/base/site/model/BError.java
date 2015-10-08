package com.yjh.base.site.model;

import java.io.Serializable;

/**
 * Created by yjh on 15-10-8.
 */
public class BError implements Serializable {
    private String message;
    private String type = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BError{" +
                "message='" + message + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

package com.yjh.base.site.entities;

import java.io.Serializable;

/**
 * error format in response
 *
 * Created by yjh on 15-10-8.
 */
public class BError implements Serializable {
    private String message;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BError{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}

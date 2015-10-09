package com.yjh.base.exception;

/**
 * some error for feedback to client
 *
 * Created by yjh on 15-10-9.
 */
public enum  BEnumError {
    VALIDATION_ERROR("验证错误", 10001);
    private String message;
    private int code;

    BEnumError(String message, int code) {
        this.message = message;
        this.code = code;
    }

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
}

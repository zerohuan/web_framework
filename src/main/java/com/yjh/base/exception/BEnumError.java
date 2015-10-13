package com.yjh.base.exception;

/**
 * some error for feedback to client
 *
 * Created by yjh on 15-10-9.
 */
public enum  BEnumError {
    VALIDATION_ERROR("验证错误", 10001),
    LOGIN_ERROR("请先登录", 10002),
    DATA_UPDATE_ERROR("数据保存异常", 10003);
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

    @Override
    public String toString() {
        return message;
    }
}

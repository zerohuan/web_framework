package com.yjh.base.util.number;

/**
 * enum of error type in number generating
 * Created by yjh on 2015/9/13.
 */
public enum NumberError {
    THREAD_UNSAFE_ERROR("Should use thread safe method instead of this unsafe method."),
    NOT_SUCH_TYPE("There is error type of generator.");

    private String msg;

    NumberError(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}

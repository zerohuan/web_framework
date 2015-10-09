package com.yjh.base.exception;

/**
 * The exception in application running redefined for unified treatment
 * It extends RuntimeException.
 *
 * Created by yjh on 15-10-9.
 */
public class BSystemException extends RuntimeException {
    public BSystemException() {
    }

    public BSystemException(String message) {
        super(message);
    }

    public BSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public BSystemException(Throwable cause) {
        super(cause);
    }

    public BSystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

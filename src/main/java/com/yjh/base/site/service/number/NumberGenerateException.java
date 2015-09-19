package com.yjh.base.site.service.number;

/**
 * exception of generation number which you could wrap other exception by.
 * Created by yjh on 2015/9/13.
 */
public class NumberGenerateException extends RuntimeException {
    public NumberGenerateException() {
        super();
    }

    public NumberGenerateException(Object message) {
        super(message.toString());
    }

}

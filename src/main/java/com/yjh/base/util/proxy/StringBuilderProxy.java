package com.yjh.base.util.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by yjh on 15-9-28.
 */
public class StringBuilderProxy implements InvocationHandler {
    private Object proxied;

    public StringBuilderProxy(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


        return null;
    }
}

package com.yjh.base.filter;

import org.apache.logging.log4j.ThreadContext;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

/**
 * This filter add the fish tag for logging.
 * Be careful use async request.
 * This filter don't care it's order, so you can use annotation.
 *
 * Created by yjh on 2015/9/17.
 */
@WebFilter(filterName = "loggingFilter", urlPatterns = "/*", dispatcherTypes = {
        DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR,
        DispatcherType.FORWARD, DispatcherType.INCLUDE})
public class LoggingFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        boolean clear = false;
        if(!ThreadContext.containsKey("id")) {
            clear = true;
            ThreadContext.put("id", UUID.randomUUID().toString());
            //TODO get username info from request parameters or sessions or other place such as database(if you put it in db).

        }

        try {
            chain.doFilter(req, resp);
        } finally {
            if(clear) {
                ThreadContext.clearAll();
            }
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}

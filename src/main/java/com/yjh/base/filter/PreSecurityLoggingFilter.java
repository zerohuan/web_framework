package com.yjh.base.filter;

import org.apache.logging.log4j.ThreadContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * Created by yjh on 15-11-2.
 */
public class PreSecurityLoggingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String id = UUID.randomUUID().toString();
        ThreadContext.put("id", id);
        try {
            ((HttpServletResponse)response).setHeader("Yjh-Request-Id", id);
            chain.doFilter(request, response);
        } finally {
            ThreadContext.remove("id");
            ThreadContext.remove("username");
        }
    }

    @Override
    public void destroy() {

    }
}

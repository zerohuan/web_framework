package com.yjh.base.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Created by yjh on 2015/9/15.
 */
@WebFilter(filterName = "asyncFilter", urlPatterns = "/*", servletNames = {"asyncServlet"}, asyncSupported = true, dispatcherTypes = DispatcherType.ASYNC)
public class AsyncFilter implements Filter {
    private String name;

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Entering AsyncFilter " + this.name + " doFilter().");

        chain.doFilter(new HttpServletRequestWrapper(((HttpServletRequest)request)),
                new HttpServletResponseWrapper((HttpServletResponse)response));

        if(request.isAsyncSupported() && request.isAsyncStarted()) {
            AsyncContext context = request.getAsyncContext();
            System.out.println("Leaving " + this.name + ".doFilter(), async " + "context holds wrapped request/response = " +
                !context.hasOriginalRequestAndResponse());
        } else {
            System.out.println("Leaving " + this.name + ".doFilter().");
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.name = filterConfig.getFilterName();
    }
}

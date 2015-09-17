package com.yjh.base.listener;

import com.yjh.base.filter.CompressFilter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * add global filters when context startup
 *
 * Created by yjh on 2015/9/16.
 */
@WebListener
public class Configurator implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        FilterRegistration.Dynamic registration = context.addFilter("compressFilter", new CompressFilter());
        registration.addMappingForUrlPatterns(null, false, "/*");
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}

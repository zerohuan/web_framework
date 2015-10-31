package com.yjh.test.forSpring.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Spring单元测试上下文初始化器
 *
 * Created by yjh on 15-10-31.
 */
public class TestInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext testContext =
                new AnnotationConfigWebApplicationContext();
        testContext.register(TestContextConfiguration.class);
        servletContext.addListener(new ContextLoaderListener(testContext));
    }
}

package com.yjh.base.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Created by yjh on 2015/9/19.
 */
@Configuration
@ComponentScan(
        basePackages = "com",
        excludeFilters = @ComponentScan.Filter(Controller.class)
)
public class RootContextConfiguration {
    private static Logger logger = LogManager.getLogger();

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        logger.debug("resolver initial.");

        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/m/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    /**
     * if you want to return model and model attribute, you need it translate model to name of view.
     * @return
     */
    @Bean
    public RequestToViewNameTranslator viewNameTranslator()
    {
        return new DefaultRequestToViewNameTranslator();
    }

    /**
     * route to user view by name
     * @return
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

}

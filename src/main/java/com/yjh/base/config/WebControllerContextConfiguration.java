package com.yjh.base.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * servlet context configuration in base framework
 *
 * Created by yjh on 2015/9/19.
 */
@Configuration
@EnableWebMvc
@ComponentScan(
        basePackages = "com.yjh.base.site",
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(Controller.class),
                @ComponentScan.Filter(ControllerAdvice.class)
        }
)
public class WebControllerContextConfiguration extends WebMvcConfigurerAdapter {
    private static Logger logger = LogManager.getLogger();

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        logger.debug("resolver initial.");

        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/b/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    /**
     * if you want to return entities and entities attribute, you need it translate entities to name of view.
     *
     */
    @Bean
    public RequestToViewNameTranslator viewNameTranslator()
    {
        return new DefaultRequestToViewNameTranslator();
    }


}

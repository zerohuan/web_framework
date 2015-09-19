package com.yjh.base.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by yjh on 2015/9/19.
 */
@Configuration
@EnableWebMvc
@ComponentScan(
        basePackages = "com.yjh.base.site",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(Controller.class)
)
public class WebControllerContextConfiguration {
}

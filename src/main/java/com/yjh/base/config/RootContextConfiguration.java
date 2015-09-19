package com.yjh.base.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

/**
 * Created by yjh on 2015/9/19.
 */
@Configuration
@ComponentScan(
        basePackages = "com.yjh.base.site",
        excludeFilters = @ComponentScan.Filter(Controller.class)
)
public class RootContextConfiguration {

}
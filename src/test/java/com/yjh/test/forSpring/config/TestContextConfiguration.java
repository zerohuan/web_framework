package com.yjh.test.forSpring.config;

import com.yjh.test.forSpring.site.testBean.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yjh on 15-10-31.
 */
@Configuration
@ComponentScan(value = "com.yjh.test.forSpring.site")
public class TestContextConfiguration {
    @Bean
    public TestA testA() {
        return new TestA();
    }

    @Bean
    public TestB testB() {
        return new TestB();
    }

    @Bean
    public TestC testC() {
        return new TestC();
    }

    @Bean
    public TestD testD() {
        return new TestD();
    }

    @Bean
    public TestE testE() {
        return new TestE();
    }
}

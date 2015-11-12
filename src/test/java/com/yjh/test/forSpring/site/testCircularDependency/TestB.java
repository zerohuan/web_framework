package com.yjh.test.forSpring.site.testCircularDependency;

import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by yjh on 15-10-31.
 */
@Component
public class TestB {
    @Inject
    private TestA testA;
    @Inject
    private TestC testC;
}

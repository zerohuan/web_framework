package com.yjh.test.forSpring.site.testBean;

import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 *
 * Created by yjh on 15-10-31.
 */
@Component
public class TestG {
    @Inject
    private TestF testF;

//    @Inject
//    public TestG(TestF testF) {
//        this.testF = testF;
//    }
}

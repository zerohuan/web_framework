package com.yjh.test.forSpring.site.testBean;

import org.springframework.stereotype.Component;

/**
 * Created by yjh on 15-10-31.
 */
@Component
public class TestF {
    private TestG testG;

//    @Inject
    public TestF(TestG testG) {
        this.testG = testG;
    }
}

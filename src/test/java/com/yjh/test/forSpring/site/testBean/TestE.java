package com.yjh.test.forSpring.site.testBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by yjh on 15-10-31.
 */
@Component
@Scope("prototype")
public class TestE {
    @Inject
    private TestD testD;
}

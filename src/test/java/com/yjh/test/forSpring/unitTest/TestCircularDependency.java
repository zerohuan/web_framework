package com.yjh.test.forSpring.unitTest;

import com.yjh.test.forSpring.config.TestContextConfiguration;
import org.junit.Test;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 测试3种不同的循环依赖
 *
 * Created by yjh on 15-10-31.
 */
public class TestCircularDependency {
    @Test
    public void testSingleton() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(TestContextConfiguration.class);
        context.refresh();
        context.getBean("testA");
    }

    @Test(expected = BeanCurrentlyInCreationException.class)
    public void testPrototype() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(TestContextConfiguration.class);
        context.refresh();
        context.getBean("testD");
    }

    @Test(expected = BeanCurrentlyInCreationException.class)
    public void testConstructor() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(TestContextConfiguration.class);
        context.refresh();
    }
}

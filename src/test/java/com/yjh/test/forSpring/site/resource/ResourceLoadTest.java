package com.yjh.test.forSpring.site.resource;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 测试Spring Resource工作方式
 *
 * Created by yjh on 15-11-4.
 */
public class ResourceLoadTest {
    private static final Logger log = LogManager.getLogger();

    @Test
    public void test() {
        try {
            Resource resource = new ClassPathResource("application-test.xml");
            InputStream inputStream = resource.getInputStream();

            String context = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            Assert.assertTrue(context != null);

            BeanFactory factory = new XmlBeanFactory(resource);
            TestA testA = (TestA)factory.getBean("testA");

            Assert.assertNotNull(testA);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

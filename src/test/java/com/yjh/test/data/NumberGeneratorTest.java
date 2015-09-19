package com.yjh.test.data;

import com.yjh.base.site.service.number.Generator;
import com.yjh.base.site.service.number.config.NormalConfig;
import com.yjh.base.site.service.number.NormalGenerator;
import com.yjh.base.site.service.number.NumberGeneratorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yjh on 2015/9/10.
 */
public class NumberGeneratorTest {
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        final Generator generator = NumberGeneratorFactory.create(new NormalConfig(true, 0, 3, Integer.MAX_VALUE, NormalGenerator.class) {
            @Override
            public int computeNext(int value) {
                if(value < end)
                    return  value + step;
                return value;
            }
        });

        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            executorService.submit(new Callable<Object>() {
                public Object call() throws Exception {
                    for (int i = 0; i < 100; i++) {
                        logger.debug("#1 " + generator.next());
                    }
                    return null;
                }
            });

            executorService.submit(new Callable<Object>() {
                public Object call() throws Exception {
                    for (int i = 0; i < 100; i++) {
                        logger.debug("#2 " + generator.next());
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}

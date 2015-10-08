package com.yjh.test.example.classload;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by yjh on 15-9-26.
 */
public class T1 {
    private static Logger logger = LogManager.getLogger();
    public T1() {
        logger.debug("Main CREATED");
    }
}

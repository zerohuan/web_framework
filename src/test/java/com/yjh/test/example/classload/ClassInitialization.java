package com.yjh.test.example.classload;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by yjh on 15-9-26.
 */
public class ClassInitialization {
    private static Logger logger = LogManager.getLogger();
    private static T1 t1 = new T1();
    private static int I;

    static {
        logger.debug("static1 execute.");
    }

    public static void main(String[] args) {
        int i = ClassInitialization.I;
    }
}

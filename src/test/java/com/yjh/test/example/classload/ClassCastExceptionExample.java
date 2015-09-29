package com.yjh.test.example.classload;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.Serializable;

/**
 * Created by yjh on 15-9-28.
 */
public class ClassCastExceptionExample {
    private static Logger logger = LogManager.getLogger();

    static class P {

    }

    static class P1 extends P {

    }

    @Test
    public void test() {
        int[] ints = new int[10];
        //Upward transition
        Serializable serializable = ints;
        Cloneable cloneable = ints;
        Object object = ints;

        Serializable serializable1 = new double[10];

        //Downward transition
        int[] ints1 = (int[])serializable;
        int[] ints2 = (int[])serializable;
        int[] ints3 = (int[])serializable;

//        P p = (P)serializable;
//        Integer integer = (Integer)serializable;
//        int[] ints1 = (int[])serializable1;


    }
}

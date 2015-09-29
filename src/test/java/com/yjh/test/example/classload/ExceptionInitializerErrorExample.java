package com.yjh.test.example.classload;

import java.util.Map;

/**
 * Created by yjh on 15-9-28.
 */
public class ExceptionInitializerErrorExample {
    private static Map<String, Integer> m;

    static {
        m.put("xx", 123);
    }

    public static void main(String[] args) {

    }

}

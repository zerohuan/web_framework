package com.yjh.test;

/**
 * some little tools for test
 *
 * Created by yjh on 15-9-23.
 */
public class TestUtil {
    public static class IdHolder {
        private static int count;
        private final int id = count++;

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "[id:" + id + "]";
        }
    }

}

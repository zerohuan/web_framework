package com.yjh.test.example.classload;

/**
 * Created by yjh on 15-9-28.
 */
public class NoLibException {
    public native void nativeMethod();

    static {
        System.loadLibrary("NoLib");
    }

    public static void main(String[] args) {
        new NoLibException().nativeMethod();
    }
}

package com.yjh.test.example.classload;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by yjh on 15-9-26.
 */
public class CustomClassLoader extends URLClassLoader {
    public CustomClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }


}

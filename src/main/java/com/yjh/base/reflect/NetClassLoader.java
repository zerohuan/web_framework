package com.yjh.base.reflect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

/**
 * Class loader for class file from net
 *
 * Created by yjh on 15-9-29.
 */
public class NetClassLoader extends ClassLoader {
    private static Logger logger = LogManager.getLogger();

    private String classPath;
    private String packageName = "com.yjh.base.site.entities";

    public NetClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> aClass = findLoadedClass(name);
        if(aClass != null) {
            return aClass;
        }

        if(name.startsWith(packageName)) {
            byte[] classData = getData(name);
            logger.debug(Arrays.toString(classData));
            if(classData == null) {
                throw new ClassNotFoundException();
            } else {
                return defineClass(name, classData, 0, classData.length);
            }
        } else {
            return super.findClass(name);
        }
    }

    private byte[] getData(String className) {
        String path = classPath + "?name=" + className;

        try {
            URL url = new URL(path);
            InputStream is = url.openStream();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int num;
            while((num = is.read(buffer)) != -1) {
                stream.write(buffer, 0, num);
            }
            return stream.toByteArray();

        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }
}

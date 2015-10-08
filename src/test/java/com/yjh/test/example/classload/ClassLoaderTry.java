package com.yjh.test.example.classload;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by yjh on 15-9-25.
 */
public class ClassLoaderTry {
    private static Logger logger = LogManager.getLogger();

    @Test
    public void test() throws Exception {
//        String path = this.getClass().getClassLoader().getResource("").toString() +
//                "com/yjh/test/Constants.class";
//        logger.debug(path);

        try(ByteArrayOutputStream stream = new ByteArrayOutputStream();
                InputStream is = new FileInputStream("/home/yjh/dms/wks/web_framework/target/test-classes/com/yjh/test/Constants.class")) {

            int num;
            byte[] buffer = new byte[2048];
            while((num = is.read(buffer)) != -1) {
                stream.write(buffer, 0, num);
            }

            logger.debug(Arrays.toString(stream.toByteArray()));

        } catch (IOException e) {
            logger.error(e);
        }

    }





}

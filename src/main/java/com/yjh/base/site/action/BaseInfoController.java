package com.yjh.base.site.action;

import com.yjh.base.reflect.NetClassLoader;
import com.yjh.base.site.service.RequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Map;

/**
 * This is a tool for development
 * Showing statuses of the program
 *
 * Created by yjh on 15-9-28.
 */
@Controller
@RequestMapping("info")
public class BaseInfoController {
    private static Logger logger = LogManager.getLogger();

    @Inject
    RequestService requestService;

    @RequestMapping(value = "request", method = RequestMethod.GET)
    public Map<String, Object> classLoad(HttpServletRequest request) {
        Map<String, Object> map = requestService.requestBaseInfo(request);

        map.put("method", request.getMethod());
        map.put("uri", request.getRequestURI());
        map.put("url", request.getRequestURL());

        return map;
    }

    /**
     * load class from net
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "classRead", method = RequestMethod.GET)
    public ResponseEntity<String> classReceive(String name) throws Exception {
        ClassLoader classLoader = new NetClassLoader("http://localhost:8080/b/info/classBytes/");

        logger.debug(name);

        Class<?> claz = classLoader.loadClass(name);

        logger.debug(claz.getCanonicalName());

//        BookTest bookTest = (BookTest)claz.newInstance();
//        bookTest.setName("yjh");

//        classLoader.loadClass("com.yjh.base.site.entities.BookTest");

        Object obj = claz.newInstance();

        claz.getMethod("printStatus").invoke(obj);


        claz.getMethod("setName", String.class).invoke(obj, "yjh");

        return new ResponseEntity<>(claz.getCanonicalName() + "<br />" +
                claz.getClassLoader().getClass().getCanonicalName() + "<br />" +
                claz.getMethod("getName").invoke(obj), HttpStatus.OK);
    }

    @RequestMapping("classBytes")
    @ResponseBody
    public byte[] classSend(String name) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try(InputStream is = new FileInputStream("/home/yjh/dms/wks/web_framework/target/classes/" +
                name.replace('.', File.separatorChar) + ".class")) {

            int num;
            byte[] buffer = new byte[2048];
            while((num = is.read(buffer)) != -1) {
                stream.write(buffer, 0, num);
            }

        } catch (IOException e) {
            logger.error(e);
        } finally {
            stream.close();
        }
        return stream.toByteArray();
    }

    @RequestMapping("test")
    @ResponseBody
    public String test() {
        return "test";
    }

}

package com.yjh.base.site.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yjh on 15-9-28.
 */
@Service
public class RequestService {
    private static Logger logger = LogManager.getLogger();

    public Map<String, Object> requestBaseInfo(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        Class<? extends HttpServletRequest> requestClass = request.getClass();

        //classLoader info
        ClassLoader classLoader = requestClass.getClassLoader();
        List<String> classLoaderNames = new ArrayList<>();
        map.put("classLoaderNames", classLoaderNames);
        while(classLoader != null) {
            classLoaderNames.add(classLoader.getClass().getCanonicalName());
            classLoader = classLoader.getParent();
        }

        return map;
    }

}

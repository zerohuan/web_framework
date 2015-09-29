package com.yjh.base.site.action;

import com.yjh.base.site.service.RequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

}

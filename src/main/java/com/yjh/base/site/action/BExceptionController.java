package com.yjh.base.site.action;

import com.yjh.base.site.entities.BResponseData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Global exception handler
 *
 * Created by yjh on 15-10-9.
 */
@ControllerAdvice
public class BExceptionController {
    private static Logger logger = LogManager.getLogger();

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public BResponseData handleRuntimeException(RuntimeException e) {
        BResponseData responseData = new BResponseData();
        responseData.setData(e);
        return responseData;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        logger.error(e);
        return "exception";
    }
}

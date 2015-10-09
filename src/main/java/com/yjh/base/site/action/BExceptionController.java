package com.yjh.base.site.action;

import com.yjh.base.site.entities.BResponseData;
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
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public BResponseData handleException(RuntimeException e) {
        BResponseData responseData = new BResponseData();
        responseData.setData(e);
        return responseData;
    }
}

package com.yjh.base.site.action;

import com.yjh.base.exception.BSystemException;
import com.yjh.base.site.entities.BResponseData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * Global exception handler
 *
 * Created by yjh on 15-10-9.
 */
@ControllerAdvice
public class BExceptionController  {
    private static Logger logger = LogManager.getLogger();
    public static final String DEFAULT_ERROR_VIEW = "404";

    @ExceptionHandler(BSystemException.class)
    @ResponseBody
    public BResponseData handleRuntimeException(BSystemException e) {
        BResponseData responseData = new BResponseData();
        responseData.setData(e);
        return responseData;
    }

    @ExceptionHandler({Exception.class, DataAccessException.class})
    public String handleException(Exception e) {
        logger.error(e);
        return "exception";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

}

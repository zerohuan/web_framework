package com.yjh.base.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This global exception resolver is not in using.
 * A ControllerAdvice supplies a global exception handler.
 *
 * @see com.yjh.base.site.action.BExceptionController
 *
 * Created by yjh on 15-10-9.
 */
public class CostumeExceptionResolver extends SimpleMappingExceptionResolver {
    private static Logger logger = LogManager.getLogger();

    @Override
    protected ModelAndView
    doResolveException(HttpServletRequest request, HttpServletResponse response,
                       Object handler, Exception ex) {

        String viewName = determineViewName(ex, request);

        if(viewName != null) {
            if (!(request.getHeader("accept").contains("application/json") || (request
                    .getHeader("X-Requested-With")!= null && request
                    .getHeader("X-Requested-With").contains("XMLHttpRequest")))) {
                // if not async request
                // Apply HTTP status code for error views, if specified.
                // Only apply it if we're processing a top-level request.
                Integer statusCode = determineStatusCode(request, viewName);
                if (statusCode != null) {
                    applyStatusCodeIfPossible(request, response, statusCode);
                }
                return getModelAndView(viewName, ex, request);
            } else {// JSON format
                try {
                    PrintWriter writer = response.getWriter();
                    writer.write(ex.getMessage());
                    writer.flush();
                } catch (IOException e) {
                    logger.error(e);
                }
                return null;
            }
        } else {
            return null;
        }
    }
}

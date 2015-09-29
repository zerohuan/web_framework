package com.yjh.base.server;

import com.yjh.cg.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by yjh on 2015/9/3.
 */
public class TestServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("get method1");
        PrintWriter out = resp.getWriter();
        out.println(System.currentTimeMillis());
        ClassLoader classLoader = this.getClass().getClassLoader();
        while(classLoader != null) {
            logger.debug(classLoader.getClass().getCanonicalName());
            classLoader = classLoader.getParent();
        }

        logger.debug("SystemClassLoader: " +
                ClassLoader.getSystemClassLoader().getClass().getCanonicalName());

        logger.debug("tomcat's class: " + req.getClass().getClassLoader().getClass().getCanonicalName());
        logger.debug("other lib: " + ViewResolver.class.getClassLoader().getClass().getCanonicalName());
        logger.debug("mine class: " + User.class.getClassLoader().getClass().getCanonicalName());

        logger.debug("Core class's: " + (List.class.getClassLoader() == null));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("post method");
        resp.getWriter().println(System.currentTimeMillis());
    }

    @Override
    public void init() throws ServletException {
        logger.debug("init method");
    }

    @Override
    public void destroy() {
        logger.debug("destroy method");
    }
}

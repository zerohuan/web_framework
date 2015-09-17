package com.yjh.base.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yjh on 2015/9/3.
 */
public class TestServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("get method1");
        resp.getWriter().println(System.currentTimeMillis());

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

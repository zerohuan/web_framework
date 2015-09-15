package com.yjh.base.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yjh on 2015/9/3.
 */
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get method1");
        resp.getWriter().print("22ss");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post method");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("init method");
    }

    @Override
    public void destroy() {
        System.out.println("destroy method");
    }
}

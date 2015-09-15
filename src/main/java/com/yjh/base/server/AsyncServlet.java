package com.yjh.base.server;

import com.yjh.base.util.number.Generator;
import com.yjh.base.util.number.NormalGenerator;
import com.yjh.base.util.number.NumberGeneratorFactory;
import com.yjh.base.util.number.config.StepNormalConfig;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * using AsyncContext
 * Created by yjh on 2015/9/15.
 */
@WebServlet(name="asyncServlet", urlPatterns = "/async", asyncSupported = true)
public class AsyncServlet extends HttpServlet {
    private Generator generator;

    @Override
    public void init() throws ServletException {
        //����ID������
        generator = NumberGeneratorFactory.create(new StepNormalConfig(true, 1, 1, Integer.MAX_VALUE, NormalGenerator.class));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        final int ID = generator.next();

        System.out.println("doGet in" + getServletName());

        long timeout = req.getParameter("timeout") == null ? 10_000L : Long.parseLong(req.getParameter("timeout"));

        final AsyncContext context = req.getParameter("unwrap") != null ? req.startAsync() : req.startAsync(req, resp);

        context.setTimeout(timeout);

        out.println("Request ID:" + ID);

        AsyncThread thread = new AsyncThread(ID, context);

        context.start(thread::doWork);

        System.out.println("Leaving servlet Request ID: " + ID + ", isAsyncStarted = " + req.isAsyncStarted());
    }

    private static class AsyncThread {
        private final int id;
        private final AsyncContext context;

        public AsyncThread(int id, AsyncContext context) {
            this.id = id;
            this.context = context;
        }

        public void doWork() {
            System.out.println("AsyncThread task is running. ID " + id);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.context.dispatch("/WEB-INF/m/async.jsp");
        }
    }

    @Override
    public void destroy() {
        generator = null;
    }
}

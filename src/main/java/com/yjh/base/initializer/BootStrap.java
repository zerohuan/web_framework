package com.yjh.base.initializer;

import com.yjh.base.config.ManagerControllerContextConfiguration;
import com.yjh.base.config.RootContextConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.File;

/**
 * Initialize Spring application context when application startUp.
 *
 * Created by yjh on 2015/9/19.
 */
public class BootStrap implements WebApplicationInitializer {
    private static Logger logger = LogManager.getLogger();

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        logger.info("Initializer startUp...");
        container.getServletRegistration("default").addMapping("/resource/*");

        //Use ContextLoaderListener to create a root context, it is father of contexts used in dispatcherServlet
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootContextConfiguration.class);
        container.addListener(new ContextLoaderListener(rootContext));

        //create a dispatcherServlet has own children context.
        //The configuration of cg project
        AnnotationConfigWebApplicationContext cgContext = new AnnotationConfigWebApplicationContext();
        cgContext.register(ManagerControllerContextConfiguration.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(cgContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        ServletRegistration.Dynamic dispatcherCG = container.addServlet("cgServlet",
                dispatcherServlet);
        dispatcherCG.setLoadOnStartup(1);
        //File upload configuration
        File path = new File(container.getRealPath("/tmp/web_yjh_files/"));

        if(path.exists() || path.mkdirs()) {
            dispatcherCG.setMultipartConfig(new MultipartConfigElement(
                    path.getAbsolutePath(), 200_971_520L, 401_943_040L, 0
            ));
        } else {
            throw new ServletException("Cannot set multipartConfig because of tmp path");
        }
        dispatcherCG.setInitParameter("projectName", "HiCG");
        dispatcherCG.addMapping("/m/*");

        //active profile
        cgContext.getEnvironment().setActiveProfiles("production");
    }
}

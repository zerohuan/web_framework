package com.yjh.cg.initializer;

import com.yjh.cg.config.ManagerControllerContextConfiguration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by yjh on 15-9-24.
 */
public class BootStrap implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container) throws ServletException {
        //The configuration of cg project
        AnnotationConfigWebApplicationContext cgContext = new AnnotationConfigWebApplicationContext();
        cgContext.register(ManagerControllerContextConfiguration.class);

        ServletRegistration.Dynamic dispatcherCG = container.addServlet("cgServlet",
                new DispatcherServlet(cgContext));
        dispatcherCG.setLoadOnStartup(1);
        dispatcherCG.addMapping("/m/*");
        cgContext.getEnvironment().setActiveProfiles("production");
    }

}

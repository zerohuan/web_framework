package com.yjh.base.initializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * add Spring Security control base on filters:
 *
 *
 * If you want to select a different method to record session,
 * override the method getSessionTrackingModes
 *
 * Created by yjh on 15-11-1.
 */
@Order(2)
public class SecurityBootStrap extends AbstractSecurityWebApplicationInitializer {
    private static Logger logger = LogManager.getLogger();

    @Override
    protected boolean enableHttpSessionEventPublisher() {
        logger.info("Executing Security BootStrap.");
        return true;
    }
}

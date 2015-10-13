package com.yjh.base.aspects;

import com.yjh.base.security.SecureValid;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 *
 *
 * Created by yjh on 15-10-11.
 */
@Aspect
public class SecureAspect {
    @Around("within(com.yjh.cg.site.controller.*) && @annotation(sv)")
    public void secureValid(ProceedingJoinPoint pjp, SecureValid sv)
            throws Exception {
        Object[] args = pjp.getArgs();




    }
}

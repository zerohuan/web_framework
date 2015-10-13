package com.yjh.base.security;

import com.yjh.base.exception.BEnumError;

import java.lang.annotation.*;

/**
 * Authentic with Spring AOP
 *
 * Created by yjh on 15-10-11.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SecureValid {
    boolean needLogin() default true;
    BEnumError errorMessage() default BEnumError.LOGIN_ERROR;

}

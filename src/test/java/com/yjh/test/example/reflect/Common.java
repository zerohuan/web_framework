package com.yjh.test.example.reflect;

import com.yjh.base.util.TypeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yjh on 15-10-8.
 */
public class Common {
    private static Logger logger = LogManager.getLogger();

    @Test
    public void test() {
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
        Type type = constraintViolations.getClass().getGenericSuperclass();
        ParameterizedType type1 = (ParameterizedType)type;
        logger.debug((type1.getActualTypeArguments())[0].getClass());
        TypeUtils.getActualTypeArguments(
                        constraintViolations.getClass().getGenericSuperclass()
        );
    }
}

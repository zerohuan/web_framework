package com.yjh.base.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by yjh on 15-10-8.
 */
public class TypeUtils {
    /**
     *
     * @param type must be a parameterized class
     * @return parameterized type of the class
     */
    public static Type[] getActualTypeArguments(Type type) {
        Type[] arguments = null;
        if(!(type instanceof ParameterizedType)) {
            throw new IllegalStateException("Unable to determine type " +
                    "arguments because not parameterized class.");
        } else {
            arguments = ((ParameterizedType) type).getActualTypeArguments();
        }
        return arguments;
    }
}

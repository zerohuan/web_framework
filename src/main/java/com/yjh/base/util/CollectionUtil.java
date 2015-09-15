package com.yjh.base.util;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by yjh on 2015/9/9.
 */
public class CollectionUtil {
    public static String printStrsMap(Map<String, String[]> map) {
        StringBuilder stringBuilder = new StringBuilder();

        for(Map.Entry<String, String[]> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(Arrays.toString(entry.getValue())).append("\r\n");
        }

        return stringBuilder.toString();
    }
}

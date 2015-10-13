package com.yjh.cg.site.service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by yjh on 15-10-12.
 */
public class ServiceUtil {
    public static <E> List<E> toList(Iterable<E> iterable) {
        List<E> list = new ArrayList<>();

        iterable.forEach(list::add);

        return list;
    }
}

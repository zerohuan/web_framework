package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BCourseStudentEntity;

import java.util.List;

/**
 * Created by yjh on 15-10-12.
 */
public interface CourseStudentService {
    List<BCourseStudentEntity> list(long userId);
}

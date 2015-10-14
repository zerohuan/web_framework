package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BCourseUserEntity;

import java.util.List;

/**
 * Created by yjh on 15-10-12.
 */
public interface CourseStudentService {
    List<BCourseUserEntity> list(long userId);
}

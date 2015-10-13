package com.yjh.cg.site.repository;

import com.yjh.base.site.repository.CustomRepository;
import com.yjh.cg.site.entities.BCourseStudentEntity;

/**
 *
 *
 * Created by yjh on 15-10-12.
 */
public interface CourseStudentRepository
        extends CustomRepository<BCourseStudentEntity, Long> {
    Iterable<BCourseStudentEntity> getByUserIdOrderByCreateDateDesc(long userId);
}

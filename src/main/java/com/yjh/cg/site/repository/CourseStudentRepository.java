package com.yjh.cg.site.repository;

import com.yjh.cg.site.entities.BCourseStudentEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 *
 * Created by yjh on 15-10-12.
 */
public interface CourseStudentRepository
        extends PagingAndSortingRepository<BCourseStudentEntity, Long> {
    Iterable<BCourseStudentEntity> getByUserIdOrderByCreateDateDesc(long userId);
}

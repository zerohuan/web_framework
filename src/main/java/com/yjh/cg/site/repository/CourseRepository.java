package com.yjh.cg.site.repository;

import com.yjh.cg.site.entities.BCourseEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * course repository
 *
 * Created by yjh on 15-10-12.
 */
public interface CourseRepository extends PagingAndSortingRepository<BCourseEntity, Long> {

}

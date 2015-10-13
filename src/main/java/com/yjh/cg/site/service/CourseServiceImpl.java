package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BCourseEntity;
import com.yjh.cg.site.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by yjh on 15-10-12.
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Inject
    CourseRepository courseRepository;

    @Transactional
    @Override
    public BCourseEntity save(BCourseEntity bCourseEntity) {
        return this.courseRepository.save(bCourseEntity);
    }
}

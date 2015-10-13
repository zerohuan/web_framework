package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BCourseEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Created by yjh on 15-10-12.
 */
@Validated
public interface CourseService {
    @NotNull(message = "{error.create.fail}")
    BCourseEntity save(BCourseEntity bCourseEntity);

//    BCourseEntity list(Long userId);
}

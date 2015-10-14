package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BCourseUserEntity;
import com.yjh.cg.site.repository.CourseStudentRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by yjh on 15-10-12.
 */
@Service
public class CourseStudentServiceImpl implements CourseStudentService {
    @Inject
    private CourseStudentRepository courseStudentRepository;

    @Override
    public List<BCourseUserEntity> list(long userId) {
        return ServiceUtil.toList(
                courseStudentRepository.getByUserIdOrderByCreateDateDesc(userId)
        );
    }
}

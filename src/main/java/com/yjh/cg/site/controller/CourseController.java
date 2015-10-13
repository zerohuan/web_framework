package com.yjh.cg.site.controller;

import com.yjh.base.exception.BRequestHandler;
import com.yjh.base.site.entities.BResponseData;
import com.yjh.cg.site.entities.BCourseEntity;
import com.yjh.cg.site.service.CourseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

/**
 * CRUD about course by teacher
 * view by student
 *
 * Created by yjh on 15-10-12.
 */
@Controller
@RequestMapping("course")
public class CourseController {
    private static Logger logger = LogManager.getLogger();

    @Inject
    private CourseService courseService;

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String courseAdd(Map<String, Object> model) {
        model.put("course", new BCourseEntity());
        return "course/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public BResponseData courseAdd(BCourseEntity courseEntity) {
        return new BRequestHandler(
                (bResponseData)->bResponseData.setData(courseService.save(courseEntity))
        ).execute();
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String courseList(Map<String, Object> model) {

        return "course/list";
    }

}

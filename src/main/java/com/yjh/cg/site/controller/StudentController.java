package com.yjh.cg.site.controller;

import com.yjh.base.exception.BSystemException;
import com.yjh.base.site.entities.BResponseData;
import com.yjh.cg.site.entities.BUserEntity;
import com.yjh.cg.site.service.CourseStudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

/**
 * student client supporter
 *
 * Created by yjh on 15-10-9.
 */
@Controller
@RequestMapping("student")
public class StudentController {
    private static Logger logger = LogManager.getLogger();

    @Inject
    private CourseStudentService courseStudentService;

    @RequestMapping(value = "models", method = RequestMethod.GET)
    @ResponseBody
    public BResponseData models() {
        throw new BSystemException("other controller");
    }

    @RequestMapping(value = "main/{id}", method = RequestMethod.GET)
    public String main(@PathVariable("id") BUserEntity user,
                                    Map<String, Object> model) {
        model.put("user", user);
        return "student/main";
    }

    @RequestMapping(value = "course/{userId}", method = RequestMethod.GET)
    public String index(@PathVariable("userId") long userId, Map<String, Object> model) {
        model.put("courses", this.courseStudentService.list(userId));
        return "student/course";
    }

}

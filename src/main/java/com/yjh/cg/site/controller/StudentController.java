package com.yjh.cg.site.controller;

import com.yjh.base.exception.BSystemException;
import com.yjh.base.site.entities.BResponseData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * student client supporter
 *
 * Created by yjh on 15-10-9.
 */
@Controller
@RequestMapping("student")
public class StudentController {
    @RequestMapping(value = "models", method = RequestMethod.GET)
    @ResponseBody
    public BResponseData models() {
        throw new BSystemException("other controller");
    }
}

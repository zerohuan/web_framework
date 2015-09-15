package com.yjh.base.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yjh on 2015/9/9.
 */
@Controller
public class LearnController {
    @RequestMapping("el")
    public String showElUsage() {
        return "el_test";
    }

    @RequestMapping("jstl")
    public String showJSTLUsage() {
        return "jstl_test";
    }
}

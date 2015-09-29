package com.yjh.cg.controller;

import com.yjh.cg.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by yjh on 15-9-20.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping(value="sample/{userId}", produces = "text/html")
    @ResponseBody
    public String sample(@PathVariable("userId") long userId,
                         @RequestParam(value = "name", defaultValue = "YJH", required = false) String name) {

        return "success<br />" + userId + " " + "name: " + name;
    }

    @RequestMapping(value="info/{userId}/{name}", method = RequestMethod.GET)
    @ModelAttribute("user")
    public String useInfo(@PathVariable Map<String, String> variables) {
        User user = new User();

        return "user/info";
    }

}

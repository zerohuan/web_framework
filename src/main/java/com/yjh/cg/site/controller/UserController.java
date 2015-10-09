package com.yjh.cg.site.controller;

import com.yjh.base.exception.BRequestHandler;
import com.yjh.base.site.model.BResponseData;
import com.yjh.cg.site.model.BUserEntity;
import com.yjh.cg.site.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

/**
 * User logic controller
 *
 * Created by yjh on 15-9-20.
 */
@Controller
@RequestMapping("user")
public class UserController {
    private static Logger logger = LogManager.getLogger();

    @Inject
    private UserService userService;

    @RequestMapping(value="sample/{userId}", produces = "text/html")
    @ResponseBody
    public String sample(@PathVariable("userId") long userId,
                         @RequestParam(value = "name", defaultValue = "YJH", required = false) String name) {

        return "success<br />" + userId + " " + "name: " + name;
    }

    @RequestMapping(value="info/{userId}/{name}", method = RequestMethod.GET)
    @ModelAttribute("user")
    public String useInfo(@PathVariable Map<String, String> variables) {
        BUserEntity user = new BUserEntity();

        return "user/info";
    }

    /**
     * sign in
     * @param username length:1-20
     * @param password length:1-20
     * @param role {@link com.yjh.cg.site.model.BRole}
     * @return user data when success, error message when fail
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    public BResponseData login(String username, String password, String role) {
        return new BRequestHandler((responseData) -> {
            BUserEntity user = UserController.this.userService.login(username, password, role);
            responseData.setData(user);
        }).execute();
    }



    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public BUserEntity test(Map<String, Object> model) {
        model.put("sdf","sdfsd");
        BUserEntity user = new BUserEntity();
        user.setIdNumber("1231313");
        return user;
    }

}

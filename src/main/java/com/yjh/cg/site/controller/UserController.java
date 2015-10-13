package com.yjh.cg.site.controller;

import com.yjh.base.exception.BRequestHandler;
import com.yjh.base.site.entities.BResponseData;
import com.yjh.cg.site.entities.BUserEntity;
import com.yjh.cg.site.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
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

    @RequestMapping(value="info/{id}", method = RequestMethod.GET)
    public String useInfo(@PathVariable(value = "id") BUserEntity userEntity,
                               Map<String, Object> model) {
        if(userEntity == null)
            throw new RuntimeException();
        model.put("user", userEntity);
        return "user/info";
    }

    @RequestMapping(value="info/{id}", method = RequestMethod.POST)
    public String useInfo(@PathVariable(value = "id") long id,
                               BUserEntity userEntity, Map<String, Object> model) {
        logger.debug(userEntity.getUsername());
        model.put("user", this.userService.save(userEntity));
        return "user/info";
    }

    /**
     * logout and clear session's attributes
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/", false, false);
    }

    /**
     * sign in
     * @param username length:1-20
     * @param password length:1-20
     * @param role {@link com.yjh.cg.site.entities.BRole}
     * @param session add automatically by spring
     * @return user data when success, error message when fail
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    public BResponseData login(String username, String password, String role,
                               final HttpSession session) {
        logger.debug(username);

        return new BRequestHandler((responseData) -> {
            BUserEntity user = UserController.this.userService.login(username, password, role);
            responseData.setData(user);

            //authentic success add username and role into session attributes
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            session.setAttribute("user", user);
        }).execute();
    }

}

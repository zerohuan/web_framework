package com.yjh.cg.site.controller;

import com.yjh.base.exception.BRequestHandler;
import com.yjh.base.site.entities.BResponseData;
import com.yjh.cg.site.entities.BUserEntity;
import com.yjh.cg.site.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

/**
 * 登录，注销等系统接口
 *
 * Created by yjh on 15-11-1.
 */
@Controller
public class SystemController {
    private static Logger logger = LogManager.getLogger();

    @Inject
    private UserService userService;

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
     * http://localhost:8080/m/user/login?username=YJH&password=123&role=SYSTEM_ADMIN
     *
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
            BUserEntity user = SystemController.this.userService.login(username, password, role);
            responseData.setData(user);

            //authentic success add username and role into session attributes
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            session.setAttribute("user", user);
        }).execute();
    }
}

package com.yjh.cg.site.controller;

import com.yjh.base.exception.BRequestHandler;
import com.yjh.base.site.entities.BResponseData;
import com.yjh.cg.site.entities.BUserEntity;
import com.yjh.cg.site.entities.BUserRole;
import com.yjh.cg.site.service.AuthenticationService;
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
import java.util.Map;

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
    @Inject
    private AuthenticationService authenticationService;

    /**
     * logout and clear session's attributes
     */
    @Deprecated
    @RequestMapping(value = "logout1", method = RequestMethod.GET)
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
    @Deprecated
    @RequestMapping(value = "login1", method = RequestMethod.GET)
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


    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public String signUp(Map<String, Object> model) {
        model.put("signUpForm", new SignUpForm());
        return "signup";
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String signUp(SignUpForm form, Map<String, Object> model) {
        BUserEntity userEntity = new BUserEntity();
        userEntity.setUsername(form.getUsername());
        userEntity.setRole(BUserRole.USER);
        BUserEntity user = authenticationService.saveUser(userEntity, form.getNewPassword());

        return "student/main/" + user.getId();
    }

    static class SignUpForm {
        private String username;
        private String newPassword;

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}

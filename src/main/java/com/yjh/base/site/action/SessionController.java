package com.yjh.base.site.action;

import com.yjh.base.site.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by yjh on 2015/9/6.
 */
@Controller
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @RequestMapping("session")
    public String showSession() {
        return "session_info";
    }

    @RequestMapping(value = "logout", produces = {"text/plain"})
    @ResponseBody
    public String logout(HttpSession session) {
        sessionService.logoutSession(session);
        return "success";
    }
}

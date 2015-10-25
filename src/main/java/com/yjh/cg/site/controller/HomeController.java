package com.yjh.cg.site.controller;

import com.yjh.base.Event.LoginEvent;
import com.yjh.base.Event.LogoutEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController
{
    @Inject ApplicationEventPublisher publisher;

    @RequestMapping("login")
    @ResponseBody
    public String login(HttpServletRequest request)
    {
        this.publisher.publishEvent(new LoginEvent(request.getContextPath()));
        return "login";
    }

    @RequestMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request)
    {
        this.publisher.publishEvent(new LogoutEvent(request.getContextPath()));
        return "logout";
    }

    @RequestMapping("/ping")
    public ResponseEntity<String> ping()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain;charset=UTF-8");
        return new ResponseEntity<>("ok", headers, HttpStatus.OK);
    }
}

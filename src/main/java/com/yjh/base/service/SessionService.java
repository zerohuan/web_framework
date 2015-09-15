package com.yjh.base.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * Created by lenovo on 2015/9/7.
 */
@Service
public class SessionService {
    public void logoutSession(HttpSession session) {
        session.invalidate();
    }
}

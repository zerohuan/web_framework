package com.yjh.base.site.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 *
 * Created by yjh on 2015/9/7.
 */
@Service
public class SessionService {
    public void logoutSession(HttpSession session) {
        session.invalidate();
    }
}

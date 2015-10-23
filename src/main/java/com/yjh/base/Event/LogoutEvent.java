package com.yjh.base.Event;

/**
 * event about logout with userId
 *
 * Created by yjh on 15-10-23.
 */
public class LogoutEvent extends AuthenticationEvent {
    public LogoutEvent(String userId) {
        super(userId);
    }
}

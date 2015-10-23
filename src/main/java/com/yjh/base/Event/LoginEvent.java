package com.yjh.base.Event;

/**
 * event about login with userId
 *
 * Created by yjh on 15-10-23.
 */
public class LoginEvent extends AuthenticationEvent {
    public LoginEvent(String userId) {
        super(userId);
    }
}

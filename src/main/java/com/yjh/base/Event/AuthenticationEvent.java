package com.yjh.base.Event;

import java.io.Serializable;

/**
 * event about authentication
 * extends ClusterEvent indicate it can be sent in cluster
 *
 * Created by yjh on 15-10-23.
 */
public abstract class AuthenticationEvent extends ClusterEvent {
    public AuthenticationEvent(Serializable source) {
        super(source);
    }
}

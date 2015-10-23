package com.yjh.base.site;

import com.yjh.base.Event.ClusterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 *
 * Created by yjh on 15-10-23.
 */
public class ClusterEventMulticaster extends SimpleApplicationEventMulticaster {
    public static final Logger log = LogManager.getLogger();
    private final Set<ClusterMessagingEndpoint> endpoints =
            new HashSet<>();
    @Inject
    ApplicationContext context;

    @Override
    public final void multicastEvent(ApplicationEvent event) {
        try {
            super.multicastEvent(event);
        } finally {
            try {
                if(event instanceof ClusterEvent &&
                        !((ClusterEvent)event).isRebroadcasted())
                    publishClusteredEvent((ClusterEvent)event);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    protected void publishClusteredEvent(ClusterEvent event) {
        synchronized (this.endpoints) {
            endpoints.forEach(e->e.send(event));
        }
    }

    protected void registerEndpoint(ClusterMessagingEndpoint endpoint) {
        if(!endpoints.contains(endpoint)) {
            synchronized (endpoints) {
                endpoints.add(endpoint);
            }
        }
    }

    protected void deregisterEndpoint(ClusterMessagingEndpoint endpoint) {
        synchronized (endpoints) {
            endpoints.remove(endpoint);
        }
    }

    protected void registerNode(String endpoint) {
        log.info("Connecting to cluster node {}.", endpoint);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            ClusterMessagingEndpoint bean =
                    context.getAutowireCapableBeanFactory()
                            .createBean(ClusterMessagingEndpoint.class);
            container.connectToServer(bean, new URI(endpoint));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            log.error("Failed to connect to cluster node {}.", endpoint, e);
        }
    }

    protected final void handleReceivedClusteredEvent(ClusterEvent event) {
        event.setRebroadcasted();
        multicastEvent(event);
    }

    @PreDestroy
    public void shutdown() {
        synchronized (endpoints) {
            endpoints.forEach(ClusterMessagingEndpoint::close);
        }
    }
}

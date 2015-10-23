package com.yjh.base.site;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 *
 *
 * Created by yjh on 15-10-23.
 */
@Service
public class ClusterManager implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LogManager.getLogger();
    private static final String HOST;
    private static final InetAddress GROUP;
    private static final int PORT = 6788;
    private static final int HOST_POST = 8080;

    private final Object mutex = new Object();
    private boolean initialized, destroyed = false;
    private String pingUrl, messagingUrl;
    private MulticastSocket socket;
    private Thread listener;
    @Inject
    ServletContext servletContext;
    @Inject
    ClusterEventMulticaster multicaster;
    static
    {
        try
        {
            HOST = InetAddress.getLocalHost().getHostAddress();
            GROUP = InetAddress.getByName("224.0.0.3");
        }
        catch (UnknownHostException e)
        {
            throw new FatalBeanException("Could not initialize IP addresses.", e);
        }
    }

    @PostConstruct
    public void listenForMulticastAnnouncements() throws Exception {
        pingUrl = "http://" + HOST + ":" + 8081 +
                servletContext.getContextPath() + "/m/ping";
        messagingUrl = "ws://" + HOST + ":" + HOST_POST +
                servletContext.getContextPath() +
                "/services/messages/a83teo83hou9883hha9";//a83teo83hou9883hha8

        synchronized(this.mutex) {
            this.socket = new MulticastSocket(PORT);
            this.socket.joinGroup(GROUP);
            this.listener = new Thread(this::listen, "cluster-listener");
            this.listener.start();
        }
    }

    private void listen() {
        byte[] buffer = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(!Thread.interrupted()) {
            try {
                socket.receive(packet);
                String url = new String(buffer, 0, packet.getLength());
                if(url.length() == 0) {
                    logger.warn("Receive blank multicast packet.");
                } else if(url.equals(messagingUrl)) {
                    logger.debug("Ignoring our own multicast packet.");
                } else
                    multicaster.registerNode(url);
            } catch (IOException e) {
                if(!destroyed)
                    logger.error(e);
                return;
            }
        }
    }

    @PreDestroy
    public void shutDownMulticastConnection() throws IOException {
        destroyed = true;
        try {
            listener.interrupt();
            socket.leaveGroup(GROUP);
        } finally {
            socket.close();
        }
    }

    @Async
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        if(this.initialized)
            return;
        this.initialized = true;

        try
        {
            URL url = new URL(this.pingUrl);
            logger.debug("Attempting to connect to self at {}.", url);
            int tries = 0;
            while(tries < 10)
            {
                tries++;
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(5000);
                try(InputStream stream = connection.getInputStream())
                {
                    String response = StreamUtils.copyToString(stream,
                            StandardCharsets.UTF_8);
                    if(response != null && response.equals("ok"))
                    {
                        logger.debug("Broadcasting multicast announcement packet.");
                        DatagramPacket packet =
                                new DatagramPacket(this.messagingUrl.getBytes(),
                                        this.messagingUrl.length(), GROUP, PORT);
                        synchronized(this.mutex)
                        {
                            this.socket.send(packet);
                        }
                        return;
                    }
                    else
                        logger.warn("Incorrect response: {}", response);
                }
                catch(Exception e)
                {
                    if(tries > 120)
                    {
                        logger.fatal("Could not connect to self within 60 seconds.",
                                e);
                        return;
                    }
                    Thread.sleep(400L);
                }
            }
        }
        catch(Exception e)
        {
            logger.fatal("Could not connect to self.", e);
        }
    }
}

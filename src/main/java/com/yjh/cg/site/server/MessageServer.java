package com.yjh.cg.site.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjh.cg.site.entities.BMessageEntity;
import com.yjh.cg.site.entities.BMessageState;
import com.yjh.cg.site.service.MessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * <h1>WebSocket server for message system</h1>
 *
 * I just depend on WebSocket API. Tomcat(8.0 and version above 8) and J2EE web servers will provide
 * specific implementation.
 *
 * <h1>WebSocket in Java</h1>
 *
 * <h1>Client:</h1>
 * Base class: {@link javax.websocket.ContainerProvider}, {@link javax.websocket.WebSocketContainer},
 * {@link javax.websocket.RemoteEndpoint}, {@link javax.websocket.Session}.
 * We can get a WebSocketContainer by ContainProvider.getWebSocketContainer();
 *
 * WebSocketContainer has 4 versions of connectToServer():
 * connectToServer can accept URI parameter, and one of POJO with @ClientEndpoint,
 * Class of POJO with @ClientEndpoint, Instance of Endpoint and Class&lt;? extends Endpoint>.
 * You need to supply a Constructor without parameters. If you use Endpoint or Class&lt;?extends Endpoint>,
 * you need to supply a ClientEndpointConfig.
 *
 * Endpoint is callback with onOpen, onClose and onError.
 * @{@link javax.websocket.ClientEndpoint} extends {@link javax.websocket.Endpoint} and has @onMessage method
 *
 * connectToServer() will return a Session, you can do many things including closing session
 * and sending message to remote terminal by Session.
 *
 * <h1>Server:</h1>
 * ServerClient extends WebSocketContainer. ServerEndpoint also can config by annotation.
 * Using ServletContext.getAttribute("javax.websocket.server.ServerContainer") will get a instance of
 * serverContainer.
 *
 * <b>But</b> most of Java EE web container will scan annotation and get class instance with @ServerEndpoint
 * automatically.
 *
 * This is my ServerEndpoint.
 *
 * Created by yjh on 15-10-15.
 */
@ServerEndpoint(value = "/message/{userId}", configurator = SpringConfigurator.class)
public class MessageServer implements DisposableBean {
    private static Map<Long, Session> userSessions = new WeakHashMap<>();
    private static ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = LogManager.getLogger();

    @Inject
    private MessageService messageService;

    @Override
    public void destroy() throws Exception {
        userSessions.clear();
    }

    /**
     * Get unread messages by userId when session opened.
     *
     * @param session session get from WebSocketContain.connectToServer
     * @param userId user's ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") long userId) {
        logger.debug("open session: userId:" + userId + " sessionId" + session.getId());

        List<BMessageEntity> unReadMessages =
                messageService.getMessageByIsRead(userId, BMessageState.UNREAD);

        if(unReadMessages.size() > 0) {
            sendJsonMessage(session, userId, unReadMessages);
        }

        userSessions.put(userId, session);
    }

    /**
     * client should send new BMessageEntity to this server, and opMessage() save data to dataBase
     *
     * @param session session get from WebSocketContain.connectToServer
     * @param byteBuffer accept messages
     */
    @OnMessage
    public void onMessage(Session session, ByteBuffer byteBuffer,
                          @PathParam("userId") long userId) {
        CharBuffer charBuffer = Charset.forName("utf8").decode(byteBuffer);
        String message = charBuffer.toString();

        try {
            MessageJson messageJson = mapper.readValue(message, MessageJson.class);

            long replyUserId = messageJson.getReplyUserId();

            //save new message
            BMessageEntity messageEntity = messageService.saveMessage(userId, replyUserId, messageJson.getMessage());
            if(messageEntity != null) {
                //send
                Session replySession = userSessions.get(replyUserId);
                if(replySession != null) {
                    sendJsonMessage(replySession, userId, messageEntity);
                }
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @OnError
    public void onError(Session session, Throwable e) {
        logger.error("sessionId:" + session.getId() + " " + e);
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") long userId) {
        Session session1 = userSessions.get(userId);
        if(session1 != null) {
            try {
                if(session1.isOpen())
                    session1.close();
            } catch (IOException e) {
                logger.error(e);
            } finally {
                userSessions.remove(userId);
            }
        }
    }

    private void sendJsonMessage(Session session, long userId, Object object) {
        try {
            session.getBasicRemote()
                    .sendText(MessageServer.mapper.writeValueAsString(object));
        } catch (IOException e) {
            handleException(e, userId);
        }
    }

    private void handleException(Throwable throwable, long userId) {
        try(Session session = userSessions.get(userId)) {
            session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.toString()));
        } catch (IOException e) {
            logger.error(e);
        } finally {
            userSessions.remove(userId);
        }
    }

    /**
     * custom configurator, TODO collect some require, then use this configurator
     */
    public static class EndpointConfigurator extends SpringConfigurator {

    }
}

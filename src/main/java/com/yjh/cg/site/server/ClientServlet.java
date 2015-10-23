package com.yjh.cg.site.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * simulate a webSocket client
 *
 * Created by yjh on 15-10-16.
 */
@ClientEndpoint
public class ClientServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();

    private static ObjectMapper mapper = new ObjectMapper();

    private Session session;
    private long userId;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        userId = Long.valueOf(this.getInitParameter("userId"));

        String path = this.getServletContext().getContextPath() + "/message/" +
                userId;

        logger.debug(path);

        try {
            URI uri = new URI("ws", "localhost:8080", path, null, null);
            session = ContainerProvider.getWebSocketContainer()
                    .connectToServer(this, uri);
            logger.debug(session.getId());
        } catch (IOException | URISyntaxException | DeploymentException e) {
            throw new ServletException("Cannot connect to " + path + "." + e);
        }
    }

    @Override
    public void destroy() {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MessageJson messageJson = new MessageJson();
        messageJson.setMessage(req.getParameter("message"));
        messageJson.setReplyUserId(Long.valueOf(req.getParameter("replyUserId")));

        try(OutputStream outputStream = session.getBasicRemote().getSendStream()) {
            mapper.writeValue(outputStream, messageJson);
            outputStream.flush();
        }
        resp.getWriter().append("OK");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println(message);
    }

    @OnClose
    public void onClose(CloseReason reason) {
        CloseReason.CloseCode code = reason.getCloseCode();


    }
}

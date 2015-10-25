package com.yjh.base.site;

import com.yjh.base.Event.ClusterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.*;

/**
 *
 * Created by yjh on 15-10-23.
 */
@ServerEndpoint(
        value = "/services/messages/{securityCode}",
        encoders = {ClusterMessagingEndpoint.Codec.class},
        decoders = {ClusterMessagingEndpoint.Codec.class},
        configurator = SpringConfigurator.class
)
@ClientEndpoint(
        encoders = {ClusterMessagingEndpoint.Codec.class},
        decoders = {ClusterMessagingEndpoint.Codec.class}
)
public class ClusterMessagingEndpoint
{
    private static Logger logger = LogManager.getLogger();
    private Session session;
    @Inject
    ClusterEventMulticaster multicaster;

    @OnOpen
    public void onOpen(Session session, @PathParam("securityCode") String securityCode)
    {
        if(!StringUtils.isEmpty(securityCode) && !ClusterManager.NODE_ID.equals(securityCode))
        {
            try
            {
                logger.error("Received connection with illegal code {}.",
                        securityCode);
                session.close(new CloseReason(
                        CloseReason.CloseCodes.VIOLATED_POLICY, "Illegal Code"
                ));
            } catch (IOException e) {
                logger.error(e);
            }
        }
        logger.debug("Successful connection ");
        this.session = session;
        this.multicaster.registerEndpoint(this);
    }

    @OnMessage
    public void receive(ClusterEvent message)
    {
        multicaster.handleReceivedClusteredEvent(message);
    }

    @OnClose
    public void close()
    {
        logger.info("Cluster node connection closed.");
        multicaster.deregisterEndpoint(this);
        if(this.session.isOpen())
        {
            try
            {
                this.session.close();
            }
            catch (IOException e)
            {
                logger.warn("Error while closing cluster node connection.", e);
            }
        }
    }

    public void send(ClusterEvent event) {
        try {
            session.getBasicRemote().sendObject(event);
        } catch (IOException | EncodeException e) {
            logger.error("send message error: " + e);
        }
    }

    public static class Codec implements Encoder.BinaryStream<ClusterEvent>,
            Decoder.BinaryStream<ClusterEvent> {
        @Override
        public ClusterEvent decode(InputStream stream)
            throws DecodeException, IOException {
            try(ObjectInputStream input = new ObjectInputStream(stream)) {
                return (ClusterEvent)input.readObject();
            } catch (ClassNotFoundException e) {
                throw new DecodeException((String)null, "Failed to decode.", e);
            }
        }

        @Override
        public void encode(ClusterEvent event, OutputStream outputStream)
                throws EncodeException, IOException {
            try(ObjectOutputStream output = new ObjectOutputStream(outputStream)) {
                output.writeObject(event);
            }
        }

        @Override
        public void init(EndpointConfig endpointConfig) {

        }

        @Override
        public void destroy() {

        }
    }
}

package com.yjh.base.Event;

import org.springframework.context.ApplicationEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * applicationEvent using in cluster
 *
 * It can be serializable and tell next node in cluster need send this event or not
 *
 * Created by yjh on 15-10-23.
 */
public class ClusterEvent extends ApplicationEvent implements Serializable {
    private final Serializable serializable;
    private boolean rebroadcasted;

    public ClusterEvent(Serializable source) {
        super(source);
        this.serializable = source;
    }

    public Serializable getSerializable() {
        return serializable;
    }

    public final boolean isRebroadcasted() {
        return rebroadcasted;
    }

    public final void setRebroadcasted() {
        this.rebroadcasted = true;
    }

    @Override
    public Object getSource() {
        return this.serializable;
    }

    /**
     * field source is transient in Parent Class(EventObject),
     * so field serializable will make event source can be serialized.
     *
     * @param in as you see
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.source = this.serializable;
    }
}

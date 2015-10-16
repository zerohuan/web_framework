package com.yjh.cg.site.entities;

import com.yjh.base.site.entities.BAuditedEntity;

import javax.persistence.*;

/**
 * Created by yjh on 15-10-15.
 */
@Entity
@Table(name = "b_message", schema = "", catalog = "cg")
public class BMessageEntity extends BAuditedEntity {
    private long userId;
    private String content;
    private long replayUserId;
    private BMessageState state;

    @Basic
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "replay_user_id")
    public long getReplayUserId() {
        return replayUserId;
    }

    public void setReplayUserId(long replayUserId) {
        this.replayUserId = replayUserId;
    }

    @Basic
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    public BMessageState getState() {
        return state;
    }

    public void setState(BMessageState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BMessageEntity)) return false;
        if (!super.equals(o)) return false;

        BMessageEntity that = (BMessageEntity) o;

        if (userId != that.userId) return false;
        if (replayUserId != that.replayUserId) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        return state == that.state;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (int) (replayUserId ^ (replayUserId >>> 32));
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}

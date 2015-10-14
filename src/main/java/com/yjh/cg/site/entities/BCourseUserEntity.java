package com.yjh.cg.site.entities;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by yjh on 15-10-12.
 */
@Entity
@Table(name = "b_course_user", schema = "", catalog = "cg")
@IdClass(BCourseUserEntityPK.class)
public class BCourseUserEntity {
    private long userId;
    private long courseId;
    private int progress;
    private String state;
    private Timestamp createDate;
    private Timestamp lastModifiedDate;

    @Id
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "course_id")
    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    @Basic
    @Column(name = "progress")
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Basic
    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Basic
    @Column(name = "date_created")
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "date_modified")
    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BCourseUserEntity that = (BCourseUserEntity) o;

        if (userId != that.userId) return false;
        if (courseId != that.courseId) return false;
        if (progress != that.progress) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (lastModifiedDate != null ? !lastModifiedDate.equals(that.lastModifiedDate) : that.lastModifiedDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (courseId ^ (courseId >>> 32));
        result = 31 * result + progress;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (lastModifiedDate != null ? lastModifiedDate.hashCode() : 0);
        return result;
    }
}

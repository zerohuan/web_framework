package com.yjh.cg.site.entities;

import com.yjh.base.site.entities.BAuditedNoIdEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 *
 * Created by yjh on 15-10-23.
 */
@Entity
@Table(name = "b_course_item_user", schema = "", catalog = "cg")
@IdClass(BCourseItemUserEntityPK.class)
public class BCourseItemUserEntity extends BAuditedNoIdEntity {
    private long userId;
    private long courseItemId;
    private int grade;
    private String state;

    @Id
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "course_item_id")
    public long getCourseItemId() {
        return courseItemId;
    }

    public void setCourseItemId(long courseItemId) {
        this.courseItemId = courseItemId;
    }

    @Basic
    @Column(name = "grade")
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Basic
    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BCourseItemUserEntity)) return false;
        if (!super.equals(o)) return false;

        BCourseItemUserEntity that = (BCourseItemUserEntity) o;

        if (userId != that.userId) return false;
        if (courseItemId != that.courseItemId) return false;
        if (grade != that.grade) return false;
        return !(state != null ? !state.equals(that.state) : that.state != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (courseItemId ^ (courseItemId >>> 32));
        result = 31 * result + grade;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}

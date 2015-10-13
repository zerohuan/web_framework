package com.yjh.cg.site.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by yjh on 15-10-12.
 */
public class BCourseStudentEntityPK implements Serializable {
    private long userId;
    private long courseId;

    @Column(name = "user_id")
    @Id
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "course_id")
    @Id
    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BCourseStudentEntityPK that = (BCourseStudentEntityPK) o;

        if (userId != that.userId) return false;
        if (courseId != that.courseId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (courseId ^ (courseId >>> 32));
        return result;
    }
}

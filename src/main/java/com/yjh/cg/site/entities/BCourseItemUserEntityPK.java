package com.yjh.cg.site.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by yjh on 15-10-23.
 */
public class BCourseItemUserEntityPK implements Serializable {
    private long userId;
    private long courseItemId;

    @Column(name = "user_id")
    @Id
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "course_item_id")
    @Id
    public long getCourseItemId() {
        return courseItemId;
    }

    public void setCourseItemId(long courseItemId) {
        this.courseItemId = courseItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BCourseItemUserEntityPK that = (BCourseItemUserEntityPK) o;

        if (userId != that.userId) return false;
        if (courseItemId != that.courseItemId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (courseItemId ^ (courseItemId >>> 32));
        return result;
    }
}

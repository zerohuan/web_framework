package com.yjh.cg.site.entities;

import com.yjh.base.site.entities.BAuditedEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by yjh on 15-10-10.
 */
@Entity
@Table(name = "b_course", schema = "", catalog = "cg")
public class BCourseEntity extends BAuditedEntity {
    private String courseName;
    private String teacherIds;
    private Timestamp startTime;
    private int courseHours;
    private List<BCourseItemEntity> courseItemEntityList;

    @Basic
    @Column(name = "course_name")
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Basic
    @Column(name = "teacher_ids")
    public String getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(String teacherIds) {
        this.teacherIds = teacherIds;
    }

    @Basic
    @Column(name = "start_time")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "course_hours")
    public int getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(int courseHours) {
        this.courseHours = courseHours;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "course_id")
    public List<BCourseItemEntity> getCourseItemEntityList() {
        return courseItemEntityList;
    }

    public void setCourseItemEntityList(List<BCourseItemEntity> courseItemEntityList) {
        this.courseItemEntityList = courseItemEntityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BCourseEntity)) return false;

        BCourseEntity that = (BCourseEntity) o;

        if (courseHours != that.courseHours) return false;
        if (!courseName.equals(that.courseName)) return false;
        if (!teacherIds.equals(that.teacherIds)) return false;
        return startTime.equals(that.startTime);

    }

    @Override
    public int hashCode() {
        int result = courseName.hashCode();
        result = 31 * result + teacherIds.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + courseHours;
        return result;
    }
}

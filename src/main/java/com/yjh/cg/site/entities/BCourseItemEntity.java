package com.yjh.cg.site.entities;

import com.yjh.base.site.entities.BAuditedEntity;

import javax.persistence.*;

/**
 * The course's items
 *
 * Created by yjh on 15-10-14.
 */
@Entity
@Table(name = "b_course_item")
public class BCourseItemEntity extends BAuditedEntity {
    private long courseId;
    private String name;
    private long fileId;
    private BCourseItemState state;
    private int rank;

    @Basic
    @Column(name = "course_id")
    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "file_id")
    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    @Enumerated(value = EnumType.STRING)
    public BCourseItemState getState() {
        return state;
    }

    public void setState(BCourseItemState state) {
        this.state = state;
    }

    @Basic
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

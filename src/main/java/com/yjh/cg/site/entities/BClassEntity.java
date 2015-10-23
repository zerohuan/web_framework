package com.yjh.cg.site.entities;

import com.yjh.base.site.entities.BAuditedEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 *
 * Created by yjh on 15-10-23.
 */
@Entity
@Table(name = "b_class", schema = "", catalog = "cg")
public class BClassEntity extends BAuditedEntity {
    private String name;
    private long teacherId;
    private String state;

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "teacher_id")
    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
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
        if (!(o instanceof BClassEntity)) return false;
        if (!super.equals(o)) return false;

        BClassEntity that = (BClassEntity) o;

        if (teacherId != that.teacherId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(state != null ? !state.equals(that.state) : that.state != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (teacherId ^ (teacherId >>> 32));
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}

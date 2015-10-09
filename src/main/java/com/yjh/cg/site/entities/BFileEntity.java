package com.yjh.cg.site.entities;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by yjh on 15-10-8.
 */
@Entity
@Table(name = "b_file", schema = "", catalog = "cg")
public class BFileEntity {
    private long id;
    private String path;
    private String belong;
    private String mimeType;
    private String filename;
    private String state;
    private int size;
    private String md5;
    private long creator;
    private Timestamp createTime;
    private Timestamp modifyTime;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Basic
    @Column(name = "belong")
    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    @Basic
    @Column(name = "mimeType")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Basic
    @Column(name = "filename")
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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
    @Column(name = "size")
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Basic
    @Column(name = "md5")
    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Basic
    @Column(name = "creator")
    public long getCreator() {
        return creator;
    }

    public void setCreator(long creator) {
        this.creator = creator;
    }

    @Basic
    @Column(name = "createTime")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "modifyTime")
    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BFileEntity that = (BFileEntity) o;

        if (id != that.id) return false;
        if (size != that.size) return false;
        if (creator != that.creator) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        if (belong != null ? !belong.equals(that.belong) : that.belong != null) return false;
        if (mimeType != null ? !mimeType.equals(that.mimeType) : that.mimeType != null) return false;
        if (filename != null ? !filename.equals(that.filename) : that.filename != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (md5 != null ? !md5.equals(that.md5) : that.md5 != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (modifyTime != null ? !modifyTime.equals(that.modifyTime) : that.modifyTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (belong != null ? belong.hashCode() : 0);
        result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + size;
        result = 31 * result + (md5 != null ? md5.hashCode() : 0);
        result = 31 * result + (int) (creator ^ (creator >>> 32));
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (modifyTime != null ? modifyTime.hashCode() : 0);
        return result;
    }
}

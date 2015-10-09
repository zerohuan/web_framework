package com.yjh.base.site.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yjh on 15-9-30.
 */
@Entity
@Table(name="BFile")
//@SequenceGenerator(name = "GlobalGenerator", sequenceName = "GlobalSequence", allocationSize = 1)
public class BFile implements Serializable {
    private long id;
    private String path;
    private String belong;
    private String mimeType;
    private String filename;
    private BFileState state;
    private BigDecimal size;
    private String md5;
    private long creator;
    private Date createTime;
    private Date modifyTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Enumerated(EnumType.STRING)
    public BFileState getState() {
        return state;
    }

    public void setState(BFileState state) {
        this.state = state;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getCreator() {
        return creator;
    }

    public void setCreator(long creator) {
        this.creator = creator;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}

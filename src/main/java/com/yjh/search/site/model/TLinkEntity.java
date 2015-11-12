package com.yjh.search.site.model;

import com.yjh.base.site.entities.BAuditedEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by yjh on 15-11-6.
 */
@Entity
@Table(name = "t_link", schema = "", catalog = "cg")
public class TLinkEntity extends BAuditedEntity {
    private String url;
    private String path;
    private String filename;
    private String keywords;

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
    @Column(name = "filename")
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Basic
    @Column(name = "keywords")
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TLinkEntity that = (TLinkEntity) o;

        return !(url != null ? !url.equals(that.url) : that.url != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}

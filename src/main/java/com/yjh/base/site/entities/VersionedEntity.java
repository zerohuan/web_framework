package com.yjh.base.site.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * common version control in entities
 *
 * Created by yjh on 15-10-13.
 */
@MappedSuperclass
public abstract class VersionedEntity extends BaseEntity {
    private long version;

    @Version
    @Column
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}

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
public abstract class BVersionedNoIdEntity {
    private long version;

    @Version
    @Column
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BVersionedNoIdEntity)) return false;
        if (!super.equals(o)) return false;

        BVersionedNoIdEntity that = (BVersionedNoIdEntity) o;

        return version == that.version;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }
}

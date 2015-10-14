package com.yjh.base.site.entities;

import com.yjh.base.convert.InstantConverter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * common Audited in entities
 *
 * Created by yjh on 15-10-13.
 */
@MappedSuperclass
public abstract class BAuditedEntity extends BVersionedEntity {
    private Instant dateCreated;
    private Instant dateModified;

    @Convert(converter = InstantConverter.class)
    @Column(name = "date_created")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Convert(converter = InstantConverter.class)
    @Column(name = "date_modified")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Instant getDateModified() {
        return dateModified;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BAuditedEntity)) return false;
        if (!super.equals(o)) return false;

        BAuditedEntity that = (BAuditedEntity) o;

        if (dateCreated != null ? !dateCreated.equals(that.dateCreated) : that.dateCreated != null) return false;
        return !(dateModified != null ? !dateModified.equals(that.dateModified) : that.dateModified != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (dateModified != null ? dateModified.hashCode() : 0);
        return result;
    }
}

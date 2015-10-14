package com.yjh.base.site.entities;

import com.yjh.base.convert.InstantConverter;

import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * common Audited in entities
 *
 * Created by yjh on 15-10-13.
 */
@MappedSuperclass
public abstract class AuditedEntity extends VersionedEntity {
    private Instant dateCreated;
    private Instant dateModified;

    @Convert(converter = InstantConverter.class)
    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Convert(converter = InstantConverter.class)
    public Instant getDateModified() {
        return dateModified;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }
}

package com.yjh.base.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * converter for Instant
 *
 * Created by yjh on 15-10-13.
 */
@Convert
public class InstantConverter implements AttributeConverter<Instant, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(Instant attribute) {
        return attribute == null ? null : new Timestamp(attribute.toEpochMilli());
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp dbData) {
        return dbData == null ? null : Instant.ofEpochMilli(dbData.getTime());
    }
}

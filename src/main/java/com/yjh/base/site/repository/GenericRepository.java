package com.yjh.base.site.repository;

import com.sun.istack.internal.NotNull;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * Created by yjh on 15-10-6.
 */
@Validated
public interface GenericRepository<I extends Serializable, E extends Serializable> {
    @NotNull Iterable<E> getAll();
    E get(@NotNull I id);
    void add(@NotNull E entity);
    void update(@NotNull E entity);
    void delete(@NotNull E entity);
    void deleteById(@NotNull I id);
}

package com.yjh.base.site.repository;

import com.sun.istack.internal.NotNull;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Base repository with CRUD operation
 * I presents the surrogate key
 * E presents the entity's type
 *
 * Created by yjh on 15-10-6.
 */
@Validated
public abstract class GenericBaseRepository<I extends Serializable, E extends Serializable>
        implements GenericRepository<I , E> {
    protected final Class<I> idClass;
    protected final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public GenericBaseRepository() {
        Type genericSuperClass = this.getClass().getGenericSuperclass();
        while(!(genericSuperClass instanceof ParameterizedType)) {
            if(!(genericSuperClass instanceof Class)) {
                throw new IllegalStateException("Unable to determine type" +
                        "arguments because generic superclass neither " +
                        "Parameterized type nor class.");
            }
            if(genericSuperClass == GenericBaseRepository.class) {
                throw new IllegalStateException("Unable to determine type" +
                        "arguments because no parameterized generic superclass " +
                        "found.");
            }
            genericSuperClass = ((Class)genericSuperClass).getGenericSuperclass();
        }
        ParameterizedType type = (ParameterizedType)genericSuperClass;
        Type[] arguments = type.getActualTypeArguments();
        this.idClass = (Class<I>)arguments[0];
        this.entityClass = (Class<E>)arguments[1];
    }

    @NotNull
    public abstract Iterable<E> getAll();
    public abstract E get(@NotNull I id);
    public abstract void add(@NotNull E entity);
    public abstract void update(@NotNull E entity);
    public abstract void delete(@NotNull E entity);
    /**
     * Be careful, the compiler cannot distinguish I and E, so you must use
     * the different function name
     *
     */
    public abstract void deleteById(@NotNull I id);
}

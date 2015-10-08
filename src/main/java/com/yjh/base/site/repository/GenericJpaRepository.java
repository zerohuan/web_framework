package com.yjh.base.site.repository;

import com.sun.istack.internal.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;

/**
 * The common class for repository supporting JPA
 *
 * Created by yjh on 15-10-6.
 */
public abstract class
        GenericJpaRepository<I extends Serializable, E extends Serializable>
        extends GenericBaseRepository<I, E> {
    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public void deleteById(@NotNull I id) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaDelete<E> query = builder.createCriteriaDelete(this.entityClass);

        this.entityManager.createQuery(query.where(builder.equal(query.from(this.entityClass).get("id"), id)));
    }

    @Override
    public void delete(@NotNull E entity) {
        this.entityManager.remove(entity);
    }

    @Override
    public void update(@NotNull E entity) {
        this.entityManager.merge(entity);
    }

    @Override
    public void add(@NotNull E entity) {
        this.entityManager.persist(entity);
    }

    @Override
    public E get(@NotNull I id) {
        return this.entityManager.find(this.entityClass, id);
    }

    @Override
    public Iterable<E> getAll() {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = builder.createQuery(this.entityClass);

        return this.entityManager.createQuery(query.select(query.from(this.entityClass))).getResultList();
    }
}

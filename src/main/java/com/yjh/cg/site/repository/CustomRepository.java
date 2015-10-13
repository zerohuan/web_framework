package com.yjh.cg.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Global repository for some common operation
 * NoRepositoryBean annotation is necessary,
 * Spring DATA should not supply a implement for it
 *
 * Created by yjh on 15-10-13.
 */
@NoRepositoryBean
public interface CustomRepository <T, ID extends Serializable>
        extends JpaRepository<T, ID> {
    void updateNotNull(T t) throws Exception;
}

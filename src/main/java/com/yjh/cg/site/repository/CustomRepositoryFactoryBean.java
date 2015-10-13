package com.yjh.cg.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * This factory bean will replace default one,
 * spring data JPA will use CustomRepositoryImpl as base repository by it
 *
 * Created by yjh on 15-10-13.
 */
public class CustomRepositoryFactoryBean <R extends JpaRepository<T, ID>, T,
        ID extends Serializable> extends JpaRepositoryFactoryBean<R, T, ID> {
    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new CustomRepositoryFactory(entityManager);
    }

    private static class CustomRepositoryFactory<T, ID extends Serializable>
            extends JpaRepositoryFactory {
        private EntityManager entityManager;

        public CustomRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
        }

        @Override
        protected Object getTargetRepository(RepositoryInformation information) {
            return new CustomRepositoryImpl<T, ID>(
                    (Class<T>)information.getDomainType(), this.entityManager
            );
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return CustomRepositoryImpl.class;
        }
    }
}

package com.yjh.base.util;

import com.yjh.base.site.repository.CustomRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * implement for custom data operation
 *
 * Created by yjh on 15-10-13.
 */
public class CustomRepositoryImpl <T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements CustomRepository<T, ID> {
    private static Logger logger = LogManager.getLogger();

    private Class<T> domainClass;
    private EntityManager entityManager;

    public CustomRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.domainClass = domainClass;
        this.entityManager = em;
    }

    public CustomRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.domainClass = entityInformation.getJavaType();
        this.entityManager = entityManager;
    }

    /**
     * update when the field is not null,
     * It just supports Entity with @Id on method,
     * not supports @EmbeddedId and @Id on field.
     *
     * @param t entity for update
     */
    @Override
    public int updateNotNull(T t) {
        int updateCount = 0;

        try {
            List<Method> idMethods = new ArrayList<>();

            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaUpdate<T> update = builder.createCriteriaUpdate(domainClass);
            Root<T> root = update.from(domainClass);

            for(Method method : domainClass.getMethods()) {
                Id annotation = method.getAnnotation(Id.class);
                Column column = method.getAnnotation(Column.class);

                if(annotation != null) {
                    //get value of id
                    idMethods.add(method);
                } else if(column != null) {
                    Object value = method.invoke(t);
                    if (value != null) {
                        update = update.set(root.get(BeanUtils.findPropertyForMethod(method).getName()), value);
                        logger.debug(BeanUtils.findPropertyForMethod(method).getName()
                                + " " + value);
                    }
                }
            }

            if(idMethods.size() >= 1) {
                for(Method m : idMethods) {
                    update = update.where(
                            builder.equal(
                                    root.get(BeanUtils.findPropertyForMethod(m).getName()),
                                    m.invoke(t))
                    );
                }
                updateCount = this.entityManager.createQuery(update).executeUpdate();
            } else {
                throw new RuntimeException("Entity class must contains one method with @Id a last.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return updateCount;
    }
}

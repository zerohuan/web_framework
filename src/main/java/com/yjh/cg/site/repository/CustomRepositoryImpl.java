package com.yjh.cg.site.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
    private Class<T> domainClass;
    private EntityManager entityManager;

    public CustomRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.domainClass = entityInformation.getJavaType();
        this.entityManager = entityManager;
    }

    public CustomRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.domainClass = domainClass;
        this.entityManager = em;
    }

    @Override
    public void updateNotNull(T t) throws Exception{
        Field[] fields = domainClass.getFields();
        List<Object> ids = new ArrayList<>();

        Annotation[] annotations = domainClass.getAnnotations();
        for(Annotation annotation : annotations) {
            if(annotation instanceof IdClass) {
                //has a composite Id

            }
        }

        for(Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(t);

        }
        if(ids.size() == 1) {
            //single field is ID
            this.entityManager.find(domainClass, ids.get(0));
        } else if(ids.size() > 0) {
            //composite ID

        } else {
            throw new Exception("Entity class must contains one field with @Id a last.");
        }
    }
}

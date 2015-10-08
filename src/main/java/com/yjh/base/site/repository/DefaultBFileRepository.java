package com.yjh.base.site.repository;

import com.yjh.base.site.model.BFile;
import org.springframework.stereotype.Repository;

/**
 * Created by yjh on 15-10-6.
 */
@Repository
public class DefaultBFileRepository extends GenericJpaRepository<Long, BFile> implements BFileRepository {

}

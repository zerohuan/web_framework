package com.yjh.cg.site.repository;

import com.yjh.base.site.repository.CustomRepository;
import com.yjh.cg.site.entities.BMessageEntity;
import com.yjh.cg.site.entities.BMessageState;

import java.util.List;

/**
 * 
 *
 * Created by yjh on 15-10-15.
 */
public interface MessageRepository extends CustomRepository<BMessageEntity, Long> {
    List<BMessageEntity> getByUserIdAndState(long userId, BMessageState state);
}

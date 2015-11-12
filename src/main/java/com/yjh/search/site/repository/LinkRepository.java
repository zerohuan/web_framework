package com.yjh.search.site.repository;

import com.yjh.base.site.repository.CustomRepository;
import com.yjh.search.site.model.TLinkEntity;

import java.util.List;

/**
 * 链接信息存库
 *
 * Created by yjh on 15-11-6.
 */
public interface LinkRepository extends CustomRepository<TLinkEntity, Long> {
    List<TLinkEntity> findByFilename(String filename);
}

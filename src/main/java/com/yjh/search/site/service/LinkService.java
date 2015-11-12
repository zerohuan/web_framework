package com.yjh.search.site.service;

import com.yjh.search.site.model.TLinkEntity;

import java.util.List;

/**
 * Created by yjh on 15-11-6.
 */
public interface LinkService {
    TLinkEntity save(TLinkEntity linkEntity);
    List<TLinkEntity> findByFilename(String filename);
}

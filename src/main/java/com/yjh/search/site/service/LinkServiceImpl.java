package com.yjh.search.site.service;

import com.yjh.search.site.model.TLinkEntity;
import com.yjh.search.site.repository.LinkRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by yjh on 15-11-6.
 */
@Service
@SuppressWarnings("unused")
public class LinkServiceImpl implements LinkService {
    @Inject
    private LinkRepository linkRepository;

    @Override
    public TLinkEntity save(TLinkEntity linkEntity) {
        return linkRepository.save(linkEntity);
    }

    @Override
    public List<TLinkEntity> findByFilename(String filename) {
        return linkRepository.findByFilename(filename);
    }
}

package com.yjh.base.site.service;

import com.yjh.base.site.entities.BFile;
import com.yjh.base.site.repository.BFileRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjh on 15-10-6.
 */
@Service
public class DefaultBFileService {
    @Inject
    BFileRepository bFileRepository;

    public List<BFile> getBFiles() {
        return this.toList(bFileRepository.getAll());
    }

    private <E> List<E> toList(Iterable<E> i) {
        List<E> list = new ArrayList<>();
        i.forEach(list::add);
        return list;
    }
}

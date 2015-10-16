package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BMessageEntity;
import com.yjh.cg.site.entities.BMessageState;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by yjh on 15-10-15.
 */
@Transactional
public interface MessageService {
    List<BMessageEntity> getMessageByIsRead(long userId, BMessageState state);
    BMessageEntity saveMessage(long userId, long reply_user_id ,String message);
}

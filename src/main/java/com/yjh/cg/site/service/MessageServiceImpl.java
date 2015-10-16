package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BMessageEntity;
import com.yjh.cg.site.entities.BMessageState;
import com.yjh.cg.site.repository.MessageRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by yjh on 15-10-15.
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Inject
    private MessageRepository messageRepository;

    @Override
    public List<BMessageEntity> getMessageByIsRead(long userId, BMessageState state) {
        return messageRepository.getByUserIdAndState(userId, state);
    }

    @Override
    public BMessageEntity saveMessage(long userId, long reply_user_id, String message) {
        BMessageEntity bMessageEntity = new BMessageEntity();
        bMessageEntity.setContent(message);
        bMessageEntity.setReplayUserId(reply_user_id);
        bMessageEntity.setUserId(userId);
        bMessageEntity.setState(BMessageState.UNREAD);
        return this.messageRepository.save(bMessageEntity);
    }
}

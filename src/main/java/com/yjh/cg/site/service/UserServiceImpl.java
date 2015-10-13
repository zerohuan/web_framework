package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BUserEntity;
import com.yjh.cg.site.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * business logic implement for user
 *
 * Created by yjh on 15-10-8.
 */
@Service
public class UserServiceImpl implements UserService {
    @Inject
    UserRepository userRepository;

    @Override
    public BUserEntity login(String username, String password, String role) {
        BUserEntity user =
                this.userRepository.getByUsernameAndPasswordAndRole(username, password, role);

        return user;
    }

    @Transactional
    @Override
    public BUserEntity save(BUserEntity bUserEntity) {
        return this.userRepository.save(bUserEntity);
    }

    @Transactional
    @Override
    public int updateNotNull(BUserEntity t) {
        return this.userRepository.updateNotNull(t);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}

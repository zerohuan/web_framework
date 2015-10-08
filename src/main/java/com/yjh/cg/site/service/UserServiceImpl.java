package com.yjh.cg.site.service;

import com.yjh.cg.site.model.BRole;
import com.yjh.cg.site.model.BUserEntity;
import com.yjh.cg.site.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 *
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


}

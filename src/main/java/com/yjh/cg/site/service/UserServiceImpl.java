package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BUserEntity;
import com.yjh.cg.site.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * business logic implement for user
 *
 * Created by yjh on 15-10-8.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Inject
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public BUserEntity login(String username, String password, String role) {
        return this.userRepository.getByUsernameAndPasswordAndRole(username, password, role);
    }

    @Override
    public BUserEntity save(BUserEntity bUserEntity) {
        return this.userRepository.save(bUserEntity);
    }

    @Override
    public int updateNotNull(BUserEntity t) {
        return this.userRepository.updateNotNull(t);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}

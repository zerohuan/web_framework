package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BUserEntity;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

/**
 * Method validation need interface
 *
 * Created by yjh on 15-10-8.
 */
@Validated
@Transactional
public interface UserService {
    @NotNull(message = "{error.login.fail}")
    BUserEntity login(@NotNull(message = "{error.login.username}") String username,
                      @NotNull(message = "{error.login.pwd}") String password,
                      @NotNull(message = "{error.login.role}") String role);
    BUserEntity save(BUserEntity bUserEntity);
    int updateNotNull(BUserEntity t);
}

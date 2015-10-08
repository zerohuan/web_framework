package com.yjh.cg.site.service;

import com.yjh.cg.site.model.BUserEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Method validation need interface
 *
 * Created by yjh on 15-10-8.
 */
@Validated
public interface UserService {
    @NotNull(message = "{error.login.fail}")
    BUserEntity login(@NotNull(message = "{error.login.username}") String username,
                      @NotNull(message = "{error.login.username}") String password,
                      @NotNull(message = "{error.login.username}") String role);
}

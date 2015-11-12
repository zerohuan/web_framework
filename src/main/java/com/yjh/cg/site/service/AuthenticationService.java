package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BUserEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 *
 * Created by yjh on 15-11-3.
 */
@Validated
public interface AuthenticationService extends AuthenticationProvider {
    @Override
    BUserEntity authenticate(Authentication authentication);

    BUserEntity saveUser(
            @NotNull(message = "{error.saveUser}")
            BUserEntity userEntity,
            String newPassword
    );
}

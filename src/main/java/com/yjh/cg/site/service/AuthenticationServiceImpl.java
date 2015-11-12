package com.yjh.cg.site.service;

import com.yjh.cg.site.entities.BUserEntity;
import com.yjh.cg.site.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * authenticate service for authenticate provider
 *
 * Created by yjh on 15-11-3.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger log = LogManager.getLogger();
    private static final SecureRandom RANDOM;
    private static final int HASHING_ROUNDS = 10;

    static
    {
        try
        {
            RANDOM = SecureRandom.getInstanceStrong();
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new IllegalStateException(e);
        }
    }

    @Inject
    private UserRepository userRepository;

    @Override
    public BUserEntity authenticate(Authentication authentication) {
        UsernamePasswordAuthenticationToken credentials =
                (UsernamePasswordAuthenticationToken)authentication;
        String username = credentials.getPrincipal().toString();
        String password = credentials.getCredentials().toString();
        credentials.eraseCredentials();

        BUserEntity principal = this.userRepository.getByUsername(username);
        if(principal == null) {
            log.warn("Authentication failed for non-existent user {}.", username);
            return null;
        }

        if(!BCrypt.checkpw(
                password,
                new String(principal.getPassword(), StandardCharsets.UTF_8)
        )) {
            log.warn("Authentication failed for non-existent user {}.", username);
            return null;
        }
        principal.setAuthenticated(true);
        log.debug("User {} successfully authenticated.", username);

        return principal;
    }

    @Override
    public BUserEntity saveUser(BUserEntity principal, String newPassword) {
        if(newPassword != null && newPassword.length() > 0)
        {
            String salt = BCrypt.gensalt(HASHING_ROUNDS, RANDOM);
            principal.setPassword(BCrypt.hashpw(newPassword, salt).getBytes());
        }

        return this.userRepository.save(principal);
    }

    @Override
    public boolean supports(Class<?> c) {
        return c == UsernamePasswordAuthenticationToken.class;
    }
}

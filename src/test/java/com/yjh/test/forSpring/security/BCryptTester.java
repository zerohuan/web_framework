package com.yjh.test.forSpring.security;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by yjh on 15-11-4.
 */
public class BCryptTester {
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

    @Test
    public void test() {
        String salt = BCrypt.gensalt(HASHING_ROUNDS, RANDOM);
        System.out.println(BCrypt.hashpw("123", salt).getBytes());
    }
}

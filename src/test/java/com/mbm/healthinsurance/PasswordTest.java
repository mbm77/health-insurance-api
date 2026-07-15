package com.mbm.healthinsurance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void generatePassword() {

        String encodedPassword =
                passwordEncoder.encode("admin123");

        System.out.println(encodedPassword);
    }
}
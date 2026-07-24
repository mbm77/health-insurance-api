package com.mbm.healthinsurance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class PasswordTest {

    @Test
    void generatePasswords() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println(encoder.encode("admin123"));
        System.out.println(encoder.encode("vikram123"));
        //$2a$10$XXsvLkf66c9YYV1EVJBfxeuYiVruB15J7SV9/Ee939G4BtBK9ekHG
       // $2a$10$lg.yAarJLx9ArwnpE0JcX.Mb2V/3J18k31odJiYaHdfr54Hor9.5q
    }
}
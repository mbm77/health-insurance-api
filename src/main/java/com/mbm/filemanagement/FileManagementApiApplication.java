package com.mbm.filemanagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mbm.filemanagement.model.User;
import com.mbm.filemanagement.repository.UserRepository;

@SpringBootApplication
public class FileManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileManagementApiApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByUsername("admin")) {

                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role("ADMIN")
                        .build();

                userRepository.save(admin);
            }
        };
    }
}
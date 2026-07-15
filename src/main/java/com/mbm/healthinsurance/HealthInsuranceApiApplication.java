package com.mbm.healthinsurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HealthInsuranceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthInsuranceApiApplication.class, args);
    }
    
    /*

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
    
    */
}
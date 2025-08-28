package com.personalfinance.finance_api;

import com.personalfinance.finance_api.domain.user.User;
import com.personalfinance.finance_api.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDataLoader {
    @Bean
    CommandLineRunner seedUsers(UserRepository users) {
        return args -> {
            if (!users.existsByEmail("test@example.com")) {
                User u = new User();
                u.setEmail("test@example.com");
                u.setPasswordHash("temp_placeholder_hash");
                users.save(u);
            }
        };
    }
}
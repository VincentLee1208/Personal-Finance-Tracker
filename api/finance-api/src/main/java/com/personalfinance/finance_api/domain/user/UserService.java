package com.personalfinance.finance_api.domain.user;

import com.personalfinance.finance_api.domain.user.dto.SignupRequest;
import com.personalfinance.finance_api.domain.user.dto.UserResponse;
import org.springframework.security.crypto.password.passwordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional
    public UserResponse signup(SignupRequest req) {
        if(users.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User u = new User();
        u.setEmail(req.getEmail());
        u.setPasswordHash(encoder.encode(req.getPassword()));
        u = users.save(u);

        return new UserResponse(u.getId(), u.getEmail());
    }
}
package com.personalfinance.finance_api.domain.user;

import com.personalfinance.finance_api.domain.user.dto.SignupRequest;
import com.personalfinance.finance_api.domain.user.dto.LoginRequest;
import com.personalfinance.finance_api.domain.user.dto.UserResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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

    public UserResponse login(LoginRequest req) {
        var user = users.findByEmail(req.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if(!encoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return new UserResponse(user.getId(), user.getEmail());
    }
}
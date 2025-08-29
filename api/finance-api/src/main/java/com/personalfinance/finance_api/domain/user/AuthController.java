package com.personalfinance.finance_api.domain.user;

import com.personalfinance.finance_api.domain.user.dto.SignupRequest;
import com.personalfinance.finance_api.domain.user.dto.LoginRequest;
import com.personalfinance.finance_api.domain.user.dto.UserResponse;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signup(@Valid @RequestBody SignupRequest req) {
        return userService.signup(req);
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest req) {
        return userService.login(req);
    }
}
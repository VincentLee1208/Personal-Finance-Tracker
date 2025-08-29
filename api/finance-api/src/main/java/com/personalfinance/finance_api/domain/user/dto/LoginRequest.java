package com.personalfinance.finance_api.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @Email @NotBlank
    private String email;

    @NotBlank
    private String password;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
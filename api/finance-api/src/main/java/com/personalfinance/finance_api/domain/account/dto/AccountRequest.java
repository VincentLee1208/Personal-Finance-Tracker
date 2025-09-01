package com.personalfinance.finance_api.domain.account.dto;
import com.personalfinance.finance_api.domain.account.AccountType;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public class AccountRequest {
    @NotNull(message = "Account type is required")
    private AccountType type;

    @Size(max = 80)
    private String customLabel;

    @NotBlank(message = "CurreNcy code is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 letters")
    private String currencyCode = "CAD";

    @Digits(integer = 17, fraction = 2)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Size(max = 40, message = "Institution code must not exceed 40 characters")
    private String institutionCode;

    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }

    public String getCustomLabel() { return customLabel; }
    public void setCustomLabel(String customLabel) { this.customLabel = customLabel; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }

    public String getInstitutionCode() { return institutionCode; }
    public void setInstitutionCode(String institutionCode) { this.institutionCode = institutionCode; }
}
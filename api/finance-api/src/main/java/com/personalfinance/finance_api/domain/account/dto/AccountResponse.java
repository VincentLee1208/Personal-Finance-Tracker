package com.personalfinance.finance_api.domain.account.dto;

import com.personalfinance.finance_api.domain.account.AccountType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class AccountResponse {
    private final Long id;
    private final AccountType type;
    private final String customLabel;
    private final String currencyCode;
    private final String institutionCode;
    private final BigDecimal currentBalance;
    private final OffsetDateTime createdAt;

    public AccountResponse(Long id, AccountType type, String customLabel, String currencyCode, String institutionCode, BigDecimal currentBalance, OffsetDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.customLabel = customLabel;
        this.currencyCode = currencyCode;
        this.institutionCode = institutionCode;
        this.currentBalance = currentBalance;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public AccountType getType() { return type; }
    public String getCustomLabel() { return customLabel; }
    public String getCurrencyCode() { return currencyCode; }
    public String getInstitutionCode() { return institutionCode; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
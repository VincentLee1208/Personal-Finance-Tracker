package com.personalfinance.finance_api.domain.transaction.dto;

import com.personalfinance.finance_api.domain.transaction.TransactionCategory;
import com.personalfinance.finance_api.domain.account.Account;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionRequest {
    private Long accountId;

    @NotNull(message = "Transaction category is required")
    private TransactionCategory category;

    @NotNull(message = "Transaction amount is required")
    @Digits(integer = 19, fraction = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Size(max = 80)
    private String description = "";

    @NotNull(message = "Transaction date is required")
    private LocalDate date;

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public TransactionCategory getCategory() { return category; }
    public void setCategory(TransactionCategory category) { this.category = category; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
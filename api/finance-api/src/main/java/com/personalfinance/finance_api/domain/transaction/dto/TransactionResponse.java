package com.personalfinance.finance_api.domain.transaction.dto;

import com.personalfinance.finance_api.domain.transaction.TransactionCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class TransactionResponse {
    private final Long id;
    private final Long accountId;
    private final String accountLabel;
    private final TransactionCategory category;
    private final String description;
    private final BigDecimal amount;
    private final LocalDate date;
    private final OffsetDateTime createdAt;

    public TransactionResponse(Long id, Long accountId, String accountLabel, TransactionCategory category, String description, BigDecimal amount, LocalDate date, OffsetDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.accountLabel = accountLabel;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }

    public Long getAccountId() { return accountId; }
    public String getAccountLabel() { return accountLabel; }
    public TransactionCategory getCategory() { return category; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
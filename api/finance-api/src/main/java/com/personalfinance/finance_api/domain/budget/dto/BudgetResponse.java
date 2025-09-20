package com.personalfinance.finance_api.domain.budget.dto;

import com.personalfinance.finance_api.domain.transaction.TransactionCategory;
import com.personalfinance.finance_api.domain.budget.BudgetFrequency;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetResponse {
    private final Long id;
    private final Long accountId;
    private final TransactionCategory category;
    private final BigDecimal amount;
    private final String currencyCode;
    private final BudgetFrequency frequency;
    private final boolean repeat;
    private final LocalDate startDate;
    private final boolean active;
    private final BigDecimal spent;

    public BudgetResponse(Long id, Long accountId, TransactionCategory category, BigDecimal amount, String currencyCode, BudgetFrequency frequency, boolean repeat, LocalDate startDate, boolean active, BigDecimal spent) {
        this.id = id;
        this.accountId = accountId;
        this.category = category;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.frequency = frequency;
        this.repeat = repeat;
        this.startDate = startDate;
        this.active = active;
        this.spent = spent;
    }

    public Long getId() { return id; }
    public Long getAccountId() { return accountId; }
    public TransactionCategory getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrencyCode() { return currencyCode; }
    public BudgetFrequency getFrequency() { return frequency; }
    public boolean isRepeat() { return repeat; }
    public LocalDate getStartDate() { return startDate; }
    public boolean isActive() { return active; }
    public BigDecimal getSpent() { return spent; }
}
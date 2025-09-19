package com.personalfinance.finance_api.domain.budget.dto;

import com.personalfinance.finance_api.domain.budget.BudgetFrequency;
import com.personalfinance.finance_api.domain.transaction.TransactionCategory;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetRequest {
    private Long accountId;

    private TransactionCategory category;

    @NotNull(message = "Budget amount is required")
    @Digits(integer = 19, fraction = 2)
    @Positive(message = "Budget amount must be positive")
    private BigDecimal amount = BigDecimal.ZERO;

    @NotNull(message = "Currency code is required")
    @Size(min = 3, max = 3, message = "Currency code must be a 3-letter ISO code")
    private String currencyCode = "CAD";

    @NotNull(message = "Budget frequency is required")
    private BudgetFrequency frequency;

    private boolean repeat;
    
    @NotNull(message = "Budget start date is required")
    private LocalDate startDate;

    private boolean active = true;

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public TransactionCategory getCategory() { return category; }
    public void setCategory(TransactionCategory category) { this.category = category; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public BudgetFrequency getFrequency() { return frequency; }
    public void setFrequency(BudgetFrequency frequency) { this.frequency = frequency; }

    public boolean isRepeat() { return repeat; }
    public void setRepeat(boolean repeat) { this.repeat = repeat; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
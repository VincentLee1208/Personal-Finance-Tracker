package com.personalfinance.finance_api.domain.budget;

import com.personalfinance.finance_api.domain.account.Account;
import com.personalfinance.finance_api.domain.user.User;
import com.personalfinance.finance_api.domain.transaction.TransactionCategory;
import com.personalfinance.finance_api.domain.budget.BudgetFrequency;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name="budgets",
    indexes = {
        @Index(name="idx_budget_user", columnList="user_id"),
        @Index(name="idx_budget_account", columnList="account_id"),
        @Index(name="idx_budget_category", columnList="category"),
        @Index(name="idx_budget_start_date", columnList="start_date")
    }
)

public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private TransactionCategory category;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode = "CAD";

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    private BudgetFrequency frequency;

    @Column(name = "repeat", nullable = false)
    private boolean repeat;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @PrePersist
    @PreUpdate
    private void validateScope() {
        if (account == null && category == null) {
            throw new IllegalStateException("Budget must be tied to either an account or a transaction category");
        }
    }

    public Long getId() { return id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

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
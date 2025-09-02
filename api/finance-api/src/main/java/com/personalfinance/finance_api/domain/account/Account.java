package com.personalfinance.finance_api.domain.account;

import com.personalfinance.finance_api.domain.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(
    name="accounts",
    indexes = {
        @Index(name="idx_account_user", columnList = "user_id"),
        @Index(name = "idx_accounts_user_type", columnList = "user_id, account_type"),
        @Index(name = "idx_accounts_user_institution", columnList = "user_id, institution_code")
    }
)

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 32)
    private AccountType type;

    @Column(name = "custom_label", length = 80)
    private String customLabel;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode = "CAD";

    @Column(name = "current_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(name = "institution_code", length = 40)
    private String institutionCode;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdAt;

    @PrePersist
    private void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public Long getId() { return id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public OffsetDateTime getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if(!(o instanceof Account)) {
            return false;
        }

        Account that = (Account) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() { 
        return Objects.hashCode(id); 
    }
}
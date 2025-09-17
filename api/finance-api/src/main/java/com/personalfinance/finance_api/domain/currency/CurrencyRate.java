package com.personalfinance.finance_api.domain.currency;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name="currency_rates"
)
public class CurrencyRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;

    @Column(name = "conversion_rate", nullable = false)
    private BigDecimal conversionRate;

    @Column(name = "fetched_date", nullable = false)
    private LocalDate dateFetched;

    public Long getId() { return id; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public BigDecimal getCurrencyRate() { return conversionRate; }
    public void setCurrencyRate(BigDecimal conversionRate) { this.conversionRate = conversionRate; }

    public LocalDate getDateFetched() { return dateFetched; }
    public void setDateFetched(LocalDate dateFetched) { this.dateFetched = dateFetched; }
}
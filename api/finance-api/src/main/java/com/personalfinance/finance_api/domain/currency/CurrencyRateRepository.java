package com.personalfinance.finance_api.domain.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    Optional<CurrencyRate> findByCurrencyCodeAndDateFetched(String currencyCode, LocalDate dateFetched);
    boolean existsByDateFetched(LocalDate dateFetched);
}
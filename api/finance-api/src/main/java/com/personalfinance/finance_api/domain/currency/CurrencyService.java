package com.personalfinance.finance_api.domain.currency;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

@Service
public class CurrencyService {
    private final CurrencyRateRepository rates;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    @Value("${rapidapi.host}")
    private String rapidApiHost;

    public CurrencyService(CurrencyRateRepository rates) {
        this.rates = rates;
    }

    @Transactional
    public void refreshRates() {
        String base = "CAD";
        String symbols = "CAD,USD,EUR,JPY,CNY,NTD";
        String url = "https://currency-conversion-and-exchange-rates.p.rapidapi.com/latest?base=" + base + "&symbols=" + symbols;

        var headers = new org.springframework.http.HttpHeaders();
        headers.set("x-rapidapi-host", rapidApiHost);
        headers.set("x-rapidapi-key", rapidApiKey);

        var entity = new org.springframework.http.HttpEntity<>(headers);

        var response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, Map.class);
        Map<String, Object> body = response.getBody();

        Map<String, Object> ratesMap = (Map<String, Object>) body.get("rates");
        LocalDate today = LocalDate.now();

        for (var entry : ratesMap.entrySet()) {
            String code = entry.getKey();
            Number rateNumber = (Number)entry.getValue();
            BigDecimal rate = BigDecimal.valueOf(rateNumber.doubleValue());

            CurrencyRate cr = new CurrencyRate();
            cr.setCurrencyCode(code);
            cr.setCurrencyRate(rate);
            cr.setDateFetched(today);

            rates.save(cr);
        }
    }

    @PostConstruct
    public void ensureRatesAvailable() {
        LocalDate today = LocalDate.now();
        if (!rates.existsByDateFetched(today)) {
            refreshRates();
        }
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledRefreshRates() {
        ensureRatesAvailable();
    }

    public BigDecimal convert(BigDecimal amount, String from, String to, LocalDate date) {
        if (from.equals(to)) { 
            return amount; 
        }

        ensureRatesAvailable();

        CurrencyRate fromRate = rates.findByCurrencyCodeAndDateFetched(from, date)
            .orElseThrow(() -> new RuntimeException("No rate for " + from));
        CurrencyRate toRate = rates.findByCurrencyCodeAndDateFetched(to, date)
            .orElseThrow(() -> new RuntimeException("No rate for " + to));

        BigDecimal amountInCAD = amount.multiply(fromRate.getCurrencyRate());
        return amountInCAD.divide(toRate.getCurrencyRate(), 2, RoundingMode.HALF_UP);
    }
}
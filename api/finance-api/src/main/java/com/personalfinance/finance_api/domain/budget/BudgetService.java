package com.personalfinance.finance_api.domain.budget;

import com.personalfinance.finance_api.domain.budget.dto.BudgetRequest;
import com.personalfinance.finance_api.domain.budget.dto.BudgetResponse;
import com.personalfinance.finance_api.domain.user.User;
import com.personalfinance.finance_api.domain.account.Account;
import com.personalfinance.finance_api.domain.account.AccountRepository;
import com.personalfinance.finance_api.domain.transaction.TransactionRepository;
import com.personalfinance.finance_api.domain.transaction.Transaction;
import com.personalfinance.finance_api.domain.currency.CurrencyService;
import com.personalfinance.finance_api.domain.Helper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {
    private final BudgetRepository budgets;
    private final AccountRepository accounts;
    private final TransactionRepository transactions;
    private final CurrencyService currencyService;
    private final Helper helper;

    public BudgetService(BudgetRepository budgets, AccountRepository accounts, TransactionRepository transactions, CurrencyService currencyService, Helper helper) {
        this.budgets = budgets;
        this.accounts = accounts;
        this.transactions = transactions;
        this.currencyService = currencyService;
        this.helper = helper;
    }

    private LocalDate getPeriodStart(Budget b) {
        LocalDate start = b.getStartDate();
        LocalDate today = LocalDate.now();

        return switch (b.getFrequency()) {
            case DAILY -> LocalDate.now();
            case WEEKLY -> {
                long weeks = ChronoUnit.WEEKS.between(start, today);
                yield start.plusWeeks(weeks);
            }
            case BIWEEKLY -> {
                long weeks = ChronoUnit.WEEKS.between(start, today);
                long biweeks = weeks / 2;
                yield start.plusWeeks(biweeks * 2);
            }
            case MONTHLY -> {
                long months = ChronoUnit.MONTHS.between(start, today);
                yield start.plusMonths(months);
            }
            case QUARTELY -> {
                long months = ChronoUnit.MONTHS.between(start, today);
                long quarters = months / 3; // every 3 months
                yield start.plusMonths(quarters * 3);
            }
            case ANNUALLY -> {
                long years = ChronoUnit.YEARS.between(start, today);
                yield start.plusYears(years);
            }
        };
    }

    private LocalDate getPeriodEnd(Budget b) {
        return switch (b.getFrequency()) {
            case DAILY -> getPeriodStart(b);
            case WEEKLY -> getPeriodStart(b).plusWeeks(1).minusDays(1);
            case BIWEEKLY -> getPeriodStart(b).plusWeeks(2).minusDays(1);
            case MONTHLY -> getPeriodStart(b).plusMonths(1).minusDays(1);
            case QUARTELY -> getPeriodStart(b).plusMonths(3).minusDays(1);
            case ANNUALLY -> getPeriodStart(b).plusYears(1).minusDays(1);
        };
    }

    private BigDecimal calculateSpent(Budget b, User user) {
        LocalDate today = LocalDate.now();

        if (b.getStartDate().isAfter(today)) {
            return BigDecimal.ZERO;
        }

        LocalDate periodStart = getPeriodStart(b);
        LocalDate periodEnd = getPeriodEnd(b);

        List<Transaction> txns = transactions.findByUserId(user.getId()).stream()
        .filter(t -> (b.getAccount() == null || (t.getAccount() != null && t.getAccount().getId().equals(b.getAccount().getId()))))
        .filter(t -> (b.getCategory() == null || t.getCategory() == b.getCategory()))
        .filter(t -> !t.getDate().isBefore(periodStart) && !t.getDate().isAfter(periodEnd))
        .toList();

        BigDecimal spent = BigDecimal.ZERO;
        for (Transaction t : txns) {
            BigDecimal absAmount = t.getAmount().abs();
            BigDecimal converted = currencyService.convert(
                absAmount,
                t.getCurrencyCode(),
                b.getCurrencyCode(),
                LocalDate.now()
            );

            if (t.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                converted = converted.negate();
            }

            System.out.println(t.getDescription());
            System.out.println(converted);
            spent = spent.add(converted);
        }

        return spent != null ? spent : BigDecimal.ZERO;
    }

    @Transactional
    public BudgetResponse createBudget(BudgetRequest req, User user) {
        Budget b = new Budget();

        b.setUser(user);
        b.setAmount(req.getAmount());
        b.setCurrencyCode(req.getCurrencyCode());
        b.setFrequency(req.getFrequency());
        b.setRepeat(req.isRepeat());
        b.setStartDate(req.getStartDate());
        b.setActive(req.isActive());
        b.setCategory(req.getCategory());

        if(req.getAccountId() != null) {
            Account account = accounts.findById(req.getAccountId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

            helper.checkOwner(account, user);
            b.setAccount(account);
        } else {
            b.setAccount(null);
        }

        budgets.save(b);

        BigDecimal spent = calculateSpent(b, user);

        return new BudgetResponse(
            b.getId(),
            b.getAccount() != null ? b.getAccount().getId() : null,
            b.getCategory(),
            b.getAmount(),
            b.getCurrencyCode(),
            b.getFrequency(),
            b.isRepeat(),
            b.getStartDate(),
            b.isActive(),
            spent != null ? spent : BigDecimal.ZERO
        );
    }

    @Transactional(readOnly = true)
    public List<BudgetResponse> getBudgetsByUser(User user) {
        List<Budget> userBudgets = budgets.findByUserId(user.getId());
        
        return userBudgets.stream().map(b -> {
            return new BudgetResponse(
                b.getId(),
                b.getAccount() != null ? b.getAccount().getId() : null,
                b.getCategory(),
                b.getAmount(),
                b.getCurrencyCode(),
                b.getFrequency(),
                b.isRepeat(),
                b.getStartDate(),
                b.isActive(),
                calculateSpent(b, user)
            );
        }).collect(Collectors.toList());
    }
}

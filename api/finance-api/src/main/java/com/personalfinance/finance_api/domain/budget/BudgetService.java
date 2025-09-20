package com.personalfinance.finance_api.domain.budget;

import com.personalfinance.finance_api.domain.budget.dto.BudgetRequest;
import com.personalfinance.finance_api.domain.budget.dto.BudgetResponse;
import com.personalfinance.finance_api.domain.user.User;
import com.personalfinance.finance_api.domain.account.Account;
import com.personalfinance.finance_api.domain.account.AccountRepository;
import com.personalfinance.finance_api.domain.Helper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BudgetService {
    private final BudgetRepository budgets;
    private final AccountRepository accounts;
    private final Helper helper;

    public BudgetService(BudgetRepository budgets, AccountRepository accounts, Helper helper) {
        this.budgets = budgets;
        this.accounts = accounts;
        this.helper = helper;
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

        return new BudgetResponse(
            b.getId(),
            b.getAccount() != null ? b.getAccount().getId() : null,
            b.getCategory(),
            b.getAmount(),
            b.getCurrencyCode(),
            b.getFrequency(),
            b.isRepeat(),
            b.getStartDate(),
            b.isActive()
        );
    }
}

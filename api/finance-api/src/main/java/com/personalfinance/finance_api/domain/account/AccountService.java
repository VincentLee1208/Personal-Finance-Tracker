package com.personalfinance.finance_api.domain.account;

import com.personalfinance.finance_api.domain.account.dto.AccountRequest;
import com.personalfinance.finance_api.domain.account.dto.AccountResponse;
import com.personalfinance.finance_api.domain.user.User;
import com.personalfinance.finance_api.domain.Helper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accounts;
    private final Helper helper;

    public AccountService(AccountRepository accounts, Helper helper) {
        this.accounts = accounts;
        this.helper = helper;
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest req, User user) {
        Account a = new Account();
        a.setUser(user);
        a.setType(req.getType());
        a.setCustomLabel(req.getCustomLabel());
        a.setCurrencyCode(req.getCurrencyCode() != null ? req.getCurrencyCode() : "CAD");
        a.setCurrentBalance(req.getCurrentBalance() != null ? req.getCurrentBalance() : BigDecimal.ZERO);
        a.setInstitutionCode(req.getInstitutionCode());

        accounts.save(a);
        return new AccountResponse(a.getId(), a.getType(), a.getCustomLabel(), a.getCurrencyCode(), a.getInstitutionCode(), a.getCurrentBalance(), a.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(Long accountId, User user) {
        Account a = accounts.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        helper.checkOwner(a, user);
        return new AccountResponse(a.getId(), a.getType(), a.getCustomLabel(), a.getCurrencyCode(), a.getInstitutionCode(), a.getCurrentBalance(), a.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByUser(User user) {
        List<Account> userAccounts = accounts.findByUserId(user.getId());
        return userAccounts.stream().map(a -> new AccountResponse(a.getId(), a.getType(), a.getCustomLabel(), a.getCurrencyCode(), a.getInstitutionCode(), a.getCurrentBalance(), a.getCreatedAt())).toList();
    }

    @Transactional
    public void deleteAccount(Long accountId, User user) {
        Account a = accounts.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        helper.checkOwner(a, user);
        accounts.delete(a);
    }

    @Transactional
    public AccountResponse updateAccount(Long accountId, AccountRequest req, User user) {
        Account a = accounts.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        helper.checkOwner(a, user);

        a.setType(req.getType());
        a.setCustomLabel(req.getCustomLabel());
        a.setCurrencyCode(req.getCurrencyCode() != null ? req.getCurrencyCode() : "CAD");
        a.setCurrentBalance(req.getCurrentBalance() != null ? req.getCurrentBalance() : BigDecimal.ZERO);
        a.setInstitutionCode(req.getInstitutionCode());

        accounts.save(a);
        return new AccountResponse(a.getId(), a.getType(), a.getCustomLabel(), a.getCurrencyCode(), a.getInstitutionCode(), a.getCurrentBalance(), a.getCreatedAt());
    }
}
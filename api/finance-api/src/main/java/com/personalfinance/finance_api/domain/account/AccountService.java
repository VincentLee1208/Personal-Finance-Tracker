package com.personalfinance.finance_api.domain.account;

import com.personalfinance.finance_api.domain.account.dto.AccountRequest;
import com.personalfinance.finance_api.domain.account.dto.AccountResponse;
import com.personalfinance.finance_api.domain.user.User;

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

    public AccountService(AccountRepository accounts) {
        this.accounts = accounts;
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
        checkOwner(a, user);
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
        checkOwner(a, user);
        accounts.delete(a);
    }

    @Transactional
    public AccountResponse updateAccount(Long accountId, AccountRequest req, User user) {
        Account a = accounts.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        checkOwner(a, user);

        a.setType(req.getType());
        a.setCustomLabel(req.getCustomLabel());
        a.setCurrencyCode(req.getCurrencyCode() != null ? req.getCurrencyCode() : "CAD");
        a.setCurrentBalance(req.getCurrentBalance() != null ? req.getCurrentBalance() : BigDecimal.ZERO);
        a.setInstitutionCode(req.getInstitutionCode());

        accounts.save(a);
        return new AccountResponse(a.getId(), a.getType(), a.getCustomLabel(), a.getCurrencyCode(), a.getInstitutionCode(), a.getCurrentBalance(), a.getCreatedAt());
    }

    private void checkOwner(Account a, User user) {
        if(!a.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this account");
        }
    }
}
package com.personalfinance.finance_api.domain.account;

import com.personalfinance.finance_api.domain.account.dto.AccountRequest;
import com.personalfinance.finance_api.domain.account.dto.AccountResponse;
import com.personalfinance.finance_api.domain.user.UserRepository;
import com.personalfinance.finance_api.domain.user.User;
import com.personalfinance.finance_api.domain.Helper;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final UserRepository users;
    private final Helper helper;

    public AccountController(AccountService accountService, UserRepository users, Helper helper) {
        this.accountService = accountService;
        this.users = users;
        this.helper = helper;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@RequestBody AccountRequest req, HttpSession session) {
        User user = helper.getSessionUser(session, users);
        return accountService.createAccount(req, user);
    }

    @GetMapping
    public List<AccountResponse> getAccounts(HttpSession session) {
        User user = helper.getSessionUser(session, users);
        return accountService.getAccountsByUser(user);
    }

    @PutMapping("/{id}")
    public AccountResponse updateAccount(@PathVariable Long id, @RequestBody AccountRequest req, HttpSession session) {
        User user = helper.getSessionUser(session, users);
        return accountService.updateAccount(id, req, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id, HttpSession session) {
        User user = helper.getSessionUser(session, users);
        accountService.deleteAccount(id, user);
    }
}
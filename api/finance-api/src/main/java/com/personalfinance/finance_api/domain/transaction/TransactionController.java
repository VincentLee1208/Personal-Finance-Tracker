package com.personalfinance.finance_api.domain.transaction;

import com.personalfinance.finance_api.domain.transaction.dto.TransactionRequest;
import com.personalfinance.finance_api.domain.transaction.dto.TransactionResponse;
import com.personalfinance.finance_api.domain.user.UserRepository;
import com.personalfinance.finance_api.domain.user.User;
import com.personalfinance.finance_api.domain.Helper;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final UserRepository users;
    private final Helper helper;

    public TransactionController(TransactionService transactionService, UserRepository users, Helper helper) {
        this.transactionService = transactionService;
        this.users = users;
        this.helper = helper;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@RequestBody TransactionRequest req, HttpSession session) {
        User user = helper.getSessionUser(session, users);
        return transactionService.createTransaction(req, user);
    }

    @GetMapping
    public List<TransactionResponse> getTransactions(HttpSession session) {
        User user = helper.getSessionUser(session, users);
        return transactionService.getTransactionsByUser(user);
    }

    @GetMapping("/accounts/{accountId}")
    public List<TransactionResponse> getTransactionsByAccount(@PathVariable Long accountId, HttpSession session) {
        User user = helper.getSessionUser(session, users);
        return transactionService.getTransactionsByAccount(accountId, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable Long id, HttpSession session) {
        User user = helper.getSessionUser(session, users);
        transactionService.deleteTransaction(id, user);
    }
}
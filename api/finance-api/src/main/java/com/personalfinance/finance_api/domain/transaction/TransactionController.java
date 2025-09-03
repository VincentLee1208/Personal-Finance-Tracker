package com.personalfinance.finance_api.domain.transaction;

import com.personalfinance.finance_api.domain.transaction.dto.TransactionRequest;
import com.personalfinance.finance_api.domain.transaction.dto.TransactionResponse;
import com.personalfinance.finance_api.domain.user.UserRepository;
import com.personalfinance.finance_api.domain.user.User;

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

    public TransactionController(TransactionService transactionService, UserRepository users) {
        this.transactionService = transactionService;
        this.users = users;
    }

    private User getSessionUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        return users.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in"));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@RequestBody TransactionRequest req, HttpSession session) {
        User user = getSessionUser(session);
        return transactionService.createTransaction(req, user);
    }

    @GetMapping
    public List<TransactionResponse> getTransactions(HttpSession session) {
        User user = getSessionUser(session);
        return transactionService.getTransactionsByUser(user);
    }
}
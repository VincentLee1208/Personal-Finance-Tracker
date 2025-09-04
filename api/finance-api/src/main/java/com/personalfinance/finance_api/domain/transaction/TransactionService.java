package com.personalfinance.finance_api.domain.transaction;

import com.personalfinance.finance_api.domain.transaction.dto.TransactionRequest;
import com.personalfinance.finance_api.domain.transaction.dto.TransactionResponse;
import com.personalfinance.finance_api.domain.user.User;
import com.personalfinance.finance_api.domain.account.Account;
import com.personalfinance.finance_api.domain.account.AccountRepository;
import com.personalfinance.finance_api.domain.Helper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactions;
    private final AccountRepository accounts;
    private final Helper helper;

    public TransactionService(TransactionRepository transactions, AccountRepository accounts, Helper helper) {
        this.transactions = transactions;
        this.accounts = accounts;
        this.helper = helper;
    }

    private TransactionResponse toResponse(Transaction t) {
        Long accountId = (t.getAccount() != null) ? t.getAccount().getId() : null;
        String accountLabel = (t.getAccount() != null) ? 
            (t.getAccount().getCustomLabel() != null ? t.getAccount().getCustomLabel() : t.getAccount().getType().name()) 
            : null;
        String accountInstitution = (t.getAccount() != null) ?
            (t.getAccount().getInstitutionCode() != null ? t.getAccount().getInstitutionCode() : "-")
            : null;

        return new TransactionResponse(
            t.getId(), 
            accountId, 
            accountLabel, 
            accountInstitution, 
            t.getCategory(), 
            t.getDescription(), 
            t.getAmount(), 
            t.getDate(), 
            t.getCreatedAt()
        );
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest req, User user) {
        Transaction t = new Transaction();
        t.setUser(user);
        t.setCategory(req.getCategory());
        t.setAmount(req.getAmount());
        t.setDescription(req.getDescription());
        t.setDate(req.getDate());

        System.out.println(user.getId());

        if(req.getAccountId() != null) {
            System.out.println("Looking for accountId=" + req.getAccountId());
            Account account = accounts.findById(req.getAccountId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
            
            if(!account.getUser().getId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this account");
            }
            
            t.setAccount(account);
        } else {
            t.setAccount(null);
        }
        transactions.save(t);

        return toResponse(t);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByUser(User user) {
        List<Transaction> userTransactions = transactions.findByUserId(user.getId());
        return userTransactions.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccount(Long accountId, User user) {
        Account a = accounts.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        helper.checkOwner(a, user);

        List<Transaction> accountTransactions = transactions.findByAccountId(accountId);
        return accountTransactions.stream().map(this::toResponse).toList();
    }
}
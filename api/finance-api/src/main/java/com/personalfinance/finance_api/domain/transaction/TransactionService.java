package com.personalfinance.finance_api.domain.transaction;

import com.personalfinance.finance_api.domain.transaction.dto.TransactionRequest;
import com.personalfinance.finance_api.domain.transaction.dto.TransactionResponse;
import com.personalfinance.finance_api.domain.user.User;
import com.personalfinance.finance_api.domain.account.Account;
import com.personalfinance.finance_api.domain.account.AccountRepository;
import com.personalfinance.finance_api.domain.currency.CurrencyService;
import com.personalfinance.finance_api.domain.Helper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactions;
    private final AccountRepository accounts;
    private final Helper helper;
    private final CurrencyService currencyService;

    public TransactionService(TransactionRepository transactions, AccountRepository accounts, Helper helper, CurrencyService currencyService) {
        this.transactions = transactions;
        this.accounts = accounts;
        this.helper = helper;
        this.currencyService = currencyService;
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
            t.getCurrencyCode(),
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
        t.setCurrencyCode(req.getCurrencyCode());
        t.setDescription(req.getDescription());
        t.setDate(req.getDate());

        if(req.getAccountId() != null) {
            System.out.println("Looking for accountId=" + req.getAccountId());
            Account account = accounts.findById(req.getAccountId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
            
            helper.checkOwner(account, user);
            
            t.setAccount(account);

            BigDecimal absAmount = req.getAmount().abs();
            BigDecimal convertedAmount = currencyService.convert(absAmount, req.getCurrencyCode(), account.getCurrencyCode(), t.getDate());

            BigDecimal newBalance;
            if(req.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                newBalance = account.getCurrentBalance().add(convertedAmount);
            } else {
                newBalance = account.getCurrentBalance().subtract(convertedAmount);
            }

            account.setCurrentBalance(newBalance);
            accounts.save(account);
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

    @Transactional
    public void deleteTransaction(Long transactionId, User user) {
        Transaction t = transactions.findById(transactionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
        helper.checkOwner(t, user);

        if(t.getAccount() != null) {
            Account account = t.getAccount();

            BigDecimal absAmount = t.getAmount().abs();
            BigDecimal convertedAmount = currencyService.convert(absAmount, t.getCurrencyCode(), account.getCurrencyCode(), t.getDate());
        
            BigDecimal newBalance;
            if(t.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                newBalance = account.getCurrentBalance().subtract(convertedAmount);
            } else {
                newBalance = account.getCurrentBalance().add(convertedAmount);
            }

            account.setCurrentBalance(newBalance);
            accounts.save(account);
        }

        transactions.delete(t);
    }
}
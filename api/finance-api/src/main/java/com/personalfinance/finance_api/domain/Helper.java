package com.personalfinance.finance_api.domain;

import com.personalfinance.finance_api.domain.account.Account;
import com.personalfinance.finance_api.domain.user.User;
import com.personalfinance.finance_api.domain.transaction.Transaction;
import com.personalfinance.finance_api.domain.user.UserRepository;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Component
public class Helper {
    public void checkOwner(Account a, User u) {
        if(!a.getUser().getId().equals(u.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this account");
        }
    }

    public void checkOwner(Transaction t, User u) {
        if (!t.getUser().getId().equals(u.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this transaction");
        }
    }

    public User getSessionUser(HttpSession session, UserRepository users) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        return users.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in"));
    }
}
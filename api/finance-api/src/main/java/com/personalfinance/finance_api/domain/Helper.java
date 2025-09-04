package com.personalfinance.finance_api.domain;

import com.personalfinance.finance_api.domain.account.Account;
import com.personalfinance.finance_api.domain.user.User;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class Helper {
    public void checkOwner(Account a, User u) {
        if(!a.getUser().getId().equals(u.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this account");
        }
    }
}
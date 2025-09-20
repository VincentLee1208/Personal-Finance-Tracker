package com.personalfinance.finance_api.domain.budget;

import com.personalfinance.finance_api.domain.budget.dto.BudgetRequest;
import com.personalfinance.finance_api.domain.budget.dto.BudgetResponse;
import com.personalfinance.finance_api.domain.user.UserRepository;
import com.personalfinance.finance_api.domain.user.User;
import com.personalfinance.finance_api.domain.Helper;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final UserRepository users;
    private final Helper helper;

    public BudgetController(BudgetService budgetService, UserRepository users, Helper helper) {
        this.budgetService = budgetService;
        this.users = users;
        this.helper = helper;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetResponse createBudget(@RequestBody BudgetRequest req, HttpSession session) {
        User user = helper.getSessionUser(session, users);
        return budgetService.createBudget(req, user);
    }
}
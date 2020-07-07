package controllers;

import factory.ExpenseFactory;
import interfaces.Expense;
import lib.Util;
import models.ExpenseMeta;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.ExpenseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private ExpenseService expenseService;
    public ExpenseController() {
        expenseService = new ExpenseService();
    }
    public String addExpense(Map<String, Map<String, Double>> expenseMap, ExpenseMeta expenseMeta) {
        ExpenseFactory expenseFactory = new ExpenseFactory();
        Expense expense = expenseFactory.getExpenseType(expenseMeta.getType());
        return expense.addExpense(expenseMap, expenseMeta);
    }
    public String addUser(Map<String, Map<String, Double>> expenseMap, Map<String, User> userMap, User user) {
        if (!isValidUser(user)) {
            logger.error("Invalid/Insufficient parameters");
            return "Invalid parameters";
        }
        if (!isUniqueUser(userMap, user)) {
            logger.error("A user with that name/phone already exists.");
            return "A user with that name/phone already exists.";
        }
        expenseMap.put(user.getId(), new HashMap<>());
        return expenseService.addUser(userMap, user);
    }
    public boolean isValidUser(User user) {
        String id = user.getId();
        String name = user.getName();
        String phone = user.getPhone();
        return Util.isValidString(id) && Util.isValidString(name) && Util.isValidString(phone) && Util.isValidPhoneNumber(phone);
    }

    public boolean isUniqueUser(Map<String, User> userMap, User user) {
        return !(userMap.containsKey(user.getId()) || userMap.values().stream().anyMatch(o -> o.getPhone().equals(user.getPhone())));
    }

    public List<String> viewBalForUser(Map<String, Map<String, Double>> expenseMap, String userId) {
        return expenseService.viewBalForUser(expenseMap, userId);
    }

    public List<String> viewAllBalances(Map<String, Map<String, Double>> expenseMap) {
        return expenseService.viewAllBalances(expenseMap);
    }
}

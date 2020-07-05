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
    public void addExpense(Map<String, Map<String, Double>> expenseMap, ExpenseMeta expenseMeta) {
        ExpenseFactory expenseFactory = new ExpenseFactory();
        Expense expense = expenseFactory.getExpenseType(expenseMeta.getType());
        expense.addExpense(expenseMap, expenseMeta);
    }
    public void addUser(Map<String, Map<String, Double>> expenseMap, Map<String, User> userMap, List<String> userInfo) {
        if (!isValidUser(userInfo)) {
            logger.error("Invalid/Insufficient parameters");
            return;
        }
        if (!isUniqueUser(userMap, userInfo.get(0), userInfo.get(2))) {
            logger.error("A user with that name/phone already exists.");
            return;
        }
        expenseService.addUser(userMap, userInfo);
        expenseMap.put(userInfo.get(0), new HashMap<>());
    }
    public boolean isValidUser(List<String> userInfo) {
        if (userInfo.size() != 3)
            return false;
        String id = userInfo.get(0);
        String name = userInfo.get(1);
        String phone = userInfo.get(2);
        return Util.isValidString(id) && Util.isValidString(name) && Util.isValidString(phone) && Util.isValidPhoneNumber(phone);
    }

    public boolean isUniqueUser(Map<String, User> userMap, String id, final String phone) {
        return !(userMap.containsKey(id) || userMap.values().stream().anyMatch(o -> o.getPhone().equals(phone)));
    }

    public void showBalanceForUser(Map<String, Map<String, Double>> expenseMap, String userId) {
        expenseService.showBalanceForUser(expenseMap, userId);
    }

    public void showAllBalances(Map<String, Map<String, Double>> expenseMap) {
        expenseService.showAllBalances(expenseMap);
    }
}

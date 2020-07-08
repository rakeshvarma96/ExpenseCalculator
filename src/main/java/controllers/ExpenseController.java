package controllers;

import factory.ExpenseFactory;
import interfaces.Expense;
import lib.Util;
import models.APIResponse;
import models.ExpenseMeta;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.ExpenseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private ExpenseService expenseService;
    private Map<String, User> userMap;
    private Map<String, Map<String, Double>> expenseMap;
    private static ExpenseController expenseController;
    private ExpenseController() {

    }
    private ExpenseController(Map<String, User> userMap, Map<String, Map<String, Double>> expenseMap) {
        this.expenseService = new ExpenseService();
        this.userMap = userMap;
        this.expenseMap = expenseMap;
    }
    public static ExpenseController getInstance() {
        if(expenseController == null) {
            synchronized (ExpenseController.class) {
                if(expenseController == null) {
                    expenseController = new ExpenseController(new HashMap<>(), new HashMap<>());
                }
            }
        }
        return expenseController;
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public Map<String, Map<String, Double>> getExpenseMap() {
        return expenseMap;
    }

    public APIResponse addUser(Map<String, Map<String, Double>> expenseMap, Map<String, User> userMap, User user) {
        if (!isValidUser(user)) {
            logger.error("Invalid/Insufficient parameters");
            return new APIResponse(400, "Invalid parameters", "failed", null);
        }
        if (!isUniqueUser(userMap, user)) {
            logger.error("A user with that name/phone already exists.");
            return new APIResponse(400, "A user with that name/phone already exists", "failed", null);
        }
        expenseMap.put(user.getId(), new HashMap<>());
        if(expenseService.addUser(userMap, user))
            return new APIResponse(200, "successfully added user", "success", null);
        return new APIResponse(500, "internal server error", "failed", null);
    }

    public APIResponse showUsers() {
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            logger.info("User id:" + entry.getKey() + ", User name:" + entry.getValue().getName());
        }
        return new APIResponse(200, "fetched users successfully", "success", new ArrayList<>(userMap.values()));
    }

    public APIResponse addExpense(Map<String, Map<String, Double>> expenseMap, ExpenseMeta expenseMeta) {
        ExpenseFactory expenseFactory = new ExpenseFactory();
        Expense expense = expenseFactory.getExpenseType(expenseMeta.getType());
        if(expense.addExpense(expenseMap, expenseMeta))
            return new APIResponse(200, "expense added successfully", "success", null);
        return new APIResponse(422, "Cannot process", "failed", null) ;
    }

    public APIResponse viewBalForUser(Map<String, Map<String, Double>> expenseMap, String userId) {
        List<String> result = expenseService.viewBalForUser(expenseMap, userId);
        return new APIResponse(200, "view balances for user is successful", "success", result);
    }

    public APIResponse viewAllBalances(Map<String, Map<String, Double>> expenseMap) {
        List<String> result = expenseService.viewAllBalances(expenseMap);
        return new APIResponse(200, "view balances for users is successful", "success", result);
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

}

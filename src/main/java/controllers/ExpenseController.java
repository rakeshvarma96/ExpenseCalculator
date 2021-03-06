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

import java.util.*;

public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private ExpenseService expenseService;
    private Map<String, User> userMap;
    private Map<String, Map<String, Double>> expenseMap;
    private Map<String, List<String>> groupMap;
    private static ExpenseController expenseController;
    private ExpenseController() {

    }
    private ExpenseController(Map<String, User> userMap, Map<String, Map<String, Double>> expenseMap, Map<String, List<String>> groupMap) {
        this.groupMap = groupMap;
        this.expenseService = new ExpenseService();
        this.userMap = userMap;
        this.expenseMap = expenseMap;
    }
    public static ExpenseController getInstance() {
        if(expenseController == null) {
            synchronized (ExpenseController.class) {
                if(expenseController == null) {
                    expenseController = new ExpenseController(new HashMap<>(), new HashMap<>(), new HashMap<>());
                }
            }
        }
        return expenseController;
    }

    public APIResponse addUser(User user) {
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

    public APIResponse addExpense(ExpenseMeta expenseMeta) {
        ExpenseFactory expenseFactory = new ExpenseFactory();
        Expense expense = expenseFactory.getExpenseType(expenseMeta.getType());
        if(expense == null)
            return new APIResponse(400, "Specify a valid type", "failed", null);
        if(expense.addExpense(expenseMap, expenseMeta))
            return new APIResponse(200, "expense added successfully", "success", null);
        return new APIResponse(422, "Cannot process", "failed", null) ;
    }

    public APIResponse viewBalForUser(String userId) {
        if(!userMap.containsKey(userId))
            return new APIResponse(400, "specified user doesn't exist", "failed", null);
        List<String> result = expenseService.viewBalForUser(expenseMap, userId);
        return new APIResponse(200, "view balances for user is successful", "success", result);
    }

    public APIResponse viewAllBalances() {
        List<String> result = expenseService.viewAllBalances(expenseMap);
        return new APIResponse(200, "view balances for users is successful", "success", result);
    }

    public APIResponse addGroup(String name, List<String> users) {
        if(isGroupExists(name) || !areUsersExist(users))
            return new APIResponse(400, "group with name already exists/users doesn't exist", "failed", null);
        if(expenseService.addGroup(groupMap, name, users))
            return new APIResponse(200, "group has successfully been created", "success", null);
        return new APIResponse(422, "Cannot process", "failed", null) ;
    }

    public APIResponse addUsersToGroup(String name, List<String> users) {
        if(!isGroupExists(name) || !areUsersExist(users) || (isGroupExists(name) && areUsersInGroup(groupMap, name, users)))
            return new APIResponse(400, "group/users doesn't exist or the group already having user(s) specified", "failed", null);
        if(expenseService.addUsersToGroup(groupMap, name, users))
            return new APIResponse(200, "users have successfully been added to the group", "success", null);
        return new APIResponse(422, "Cannot process", "failed", null) ;
    }

    public APIResponse viewAllGroups() {
        return new APIResponse(200, "view all groups is success", "success", groupMap);
    }

    public APIResponse addGroupExpense(ExpenseMeta expenseMeta) {
        String groupName = expenseMeta.getGroupName();
        List<String> participants = expenseMeta.getParticipants() != null?expenseMeta.getParticipants():groupMap.get(groupName);
        expenseMeta.setParticipants(participants);
        if(!isGroupExists(groupName) || !areUsersExist(participants) || !areUsersInGroup(groupMap, groupName, participants))
            return new APIResponse(400, "group/users doesn't exist or the group doesn't have user(s) specified", "failed", null);
        return addExpense(expenseMeta);
    }

    public APIResponse viewGroupBalances(String groupName) {
        if(!groupMap.containsKey(groupName))
            return new APIResponse(400, "specified group doesn't exist", "failed", null);
        Map<String, List<String>> result = expenseService.viewGroupBalances(groupMap, groupName, expenseMap);
        return new APIResponse(200, "view group expenses is success", "success", result);
    }

    public APIResponse viewAllGroupBalances() {
        Map<String, List<String>> result = expenseService.viewAllGroupBalances(groupMap, expenseMap);
        return new APIResponse(200, "view all groups expenses is success", "success", result);
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

    public boolean isGroupExists(String name) {
        return groupMap.containsKey(name);
    }

    public boolean areUsersExist(List<String> users) {
        return userMap.keySet().containsAll(users);
    }

    public boolean areUsersInGroup(Map<String, List<String>> groupMap, String name, List<String> users) {
        return !groupMap.get(name).isEmpty() && !users.isEmpty() && !Collections.disjoint(groupMap.get(name), users);
    }
}

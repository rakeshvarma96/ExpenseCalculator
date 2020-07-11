package services;

import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseService {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    public boolean addUser(Map<String, User> userMap, User user) {
        userMap.put(user.getId(), user);
        return true;
    }

    public List<String> viewBalForUser(Map<String, Map<String, Double>> expenseMap, String userId) {
        Map<String, Double> userExpDet = expenseMap.get(userId);
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Double> entry : userExpDet.entrySet()) {
            double amount = entry.getValue();
            if (amount > 0)
                result.add(userId + " gets " + amount + " from " + entry.getKey());
            else if (amount < 0)
                result.add(userId + " owes " + entry.getKey() + " " + Math.abs(amount));
            else
                result.add(userId + " is settled");
        }
        return result;
    }

    public List<String> viewAllBalances(Map<String, Map<String, Double>> expenseMap) {
        List<String> res = new ArrayList<>();
        for (Map.Entry<String, Map<String, Double>> entry : expenseMap.entrySet()) {
            res.addAll(viewBalForUser(expenseMap, entry.getKey()));
        }
        return res;
    }

    public boolean addGroup(Map<String, List<String>> groupMap, String name, List<String> users) {
        groupMap.put(name, users);
        return true;
    }

    public boolean addUsersToGroup(Map<String, List<String>> groupMap, String name, List<String> users) {
        List<String> existingUsers = groupMap.get(name);
        existingUsers.addAll(users);
        groupMap.put(name, existingUsers);
        return true;
    }

    public Map<String, List<String>> viewGroupBalances(Map<String, List<String>> groupMap, String groupName, Map<String, Map<String, Double>> expenseMap) {
        Map<String, List<String>> groupBalances = new HashMap<>();
        List<String> balances = new ArrayList<>();
        for(String participant: groupMap.get(groupName)) {
            balances.addAll(viewBalForUser(expenseMap, participant));
        }
        groupBalances.put(groupName, balances);
        return groupBalances;
    }

    public Map<String, List<String>> viewAllGroupBalances(Map<String, List<String>> groupMap, Map<String, Map<String, Double>> expenseMap) {
        Map<String, List<String>> allGroupBalances = new HashMap<>();
        for(Map.Entry<String, List<String>> entry: groupMap.entrySet()) {
            String groupName = entry.getKey();
            List<String> expenses = new ArrayList<>();
            for(String participant: entry.getValue()) {
                expenses.addAll(viewBalForUser(expenseMap, participant));
            }
            allGroupBalances.put(groupName, expenses);
        }
        return allGroupBalances;
    }

}

package services;

import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
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
                result.add(userId + " owes " + entry.getKey() + " " + -1 * amount);
            else
                result.add("You are settled");
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
}

package services;

import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ExpenseService {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    public void addUser(Map<String, User> userMap, List<String> userInfo) {
        userMap.put(userInfo.get(0), new User(userInfo.get(0), userInfo.get(1), userInfo.get(2)));
    }

    public void showBalanceForUser(Map<String, Map<String, Double>> expenseMap, String userId) {
        Map<String, Double> userExpDet = expenseMap.get(userId);
        for (Map.Entry<String, Double> entry : userExpDet.entrySet()) {
            double amount = entry.getValue();
            if (amount > 0)
                logger.info(userId + " gets " + amount + " from " + entry.getKey());
            else if (amount < 0)
                logger.info(userId + " owes " + entry.getKey() + " " + -1 * amount);
            else
                logger.info("You are settled");
        }
    }

    public void showAllBalances(Map<String, Map<String, Double>> expenseMap) {
        for (Map.Entry<String, Map<String, Double>> entry : expenseMap.entrySet()) {
            showBalanceForUser(expenseMap, entry.getKey());
        }
    }
}

package entry;

import controllers.ExpenseController;
import models.ExpenseMeta;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseMain {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseMain.class);
    private Map<String, User> userMap;
    private Map<String, Map<String, Double>> expenseMap;
    private ExpenseController expenseController;
    private static ExpenseMain expenseMain;
    private ExpenseMain() {

    }
    private ExpenseMain(Map<String, User> userMap, Map<String, Map<String, Double>> expenseMap) {
        this.userMap = userMap;
        this.expenseMap = expenseMap;
        expenseController = new ExpenseController();
    }
    public static ExpenseMain getInstance() {
        if (expenseMain == null) {
            synchronized (ExpenseMain.class) {
                if (expenseMain == null) {
                    expenseMain = new ExpenseMain(new HashMap<>(), new HashMap<>());
                }
            }
        }
        return expenseMain;
    }
    public String addUser(User user) {
        return expenseController.addUser(expenseMap, userMap, user);
    }
    public List<User> showUsers() {
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            logger.info("User id:" + entry.getKey() + ", User name:" + entry.getValue().getName());
        }
        return userMap.values().stream().collect(Collectors.toList());
    }
    public String addExpense(ExpenseMeta expenseMeta) {
        return expenseController.addExpense(expenseMap, expenseMeta);
    }
    public List<String> viewBalForUser(String userId) {
        return expenseController.viewBalForUser(expenseMap, userId);
    }
    public List<String> viewAllBalances() {
        return expenseController.viewAllBalances(expenseMap);
    }
}

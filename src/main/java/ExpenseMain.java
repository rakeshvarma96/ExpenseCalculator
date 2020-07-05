import controllers.ExpenseController;
import lib.Constants;
import models.ExpenseMeta;
import models.User;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpenseMain {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseMain.class);
    private Map<String, User> userMap;
    private Map<String, Map<String, Double>> expenseMap;
    private ExpenseController expenseController;

    public ExpenseMain(Map<String, User> userMap, Map<String, Map<String, Double>> expenseMap) {
        this.userMap = userMap;
        this.expenseMap = expenseMap;
        expenseController = new ExpenseController();
    }

    public static void main(String[] args) {
        ExpenseMain expenseMain = new ExpenseMain(new HashMap<>(), new HashMap<>());
        Scanner scanner = new Scanner(System.in);
        while(true) {
            expenseMain.printSelectInfo();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    expenseMain.addUser(scanner);
                    break;
                case 2:
                    expenseMain.showUsers();
                    break;
                case 3:
                    expenseMain.addExpense(scanner);
                    break;
                case 4:
                    expenseMain.showBalanceForUser(scanner);
                    break;
                case 5:
                    expenseMain.showAllBalances();
                    break;
                case 6:
                    break;
            }
            if(choice == 6)
                break;
        }
    }
    public void printSelectInfo() {
        logger.info("Select either of below choices");
        logger.info("1. Onboard User");
        logger.info("2. Show Users");
        logger.info("3. Add Expense");
        logger.info("4. Show Balance For User With Id");
        logger.info("5. Show All Balances");
        logger.info("6. Kill Me");
    }
    public void addUser(Scanner scanner) {
        logger.info("Enter id");
        String id = scanner.next();
        logger.info("Enter name");
        String name = scanner.next();
        logger.info("Enter phone");
        String phone = scanner.next();
        List<String> userInfo = Arrays.asList(id, name, phone);
        expenseController.addUser(expenseMap, userMap, userInfo);
    }
    public void showUsers() {
        for(Map.Entry<String, User> entry: userMap.entrySet()) {
            logger.info("User id:"+entry.getKey()+", User name:"+entry.getValue().getName());
        }
    }
    public void addExpense(Scanner scanner) {
        showUsers();
        logger.info("Enter expense name");
        String expenseName = scanner.next();
        logger.info("Enter a little notes about this expense");
        String description = scanner.next();
        logger.info("Enter user id who paid");
        String paidBy = scanner.next();
        logger.info("Enter comma separated participants");
        List<String> participants = Arrays.asList(scanner.next().split(","));
        logger.info("Enter amount paid");
        double amount = scanner.nextDouble();
        logger.info("Enter expense type i.e., equal or percent");
        String type = scanner.next();
        List<Double> contributions = null;
        if(type.equals(Constants.PERCENT)) {
            logger.info("Enter percentages with comma separated for the participants involved");
            contributions = Arrays.stream(scanner.next().split(",")).map(Double::valueOf).collect(Collectors.toList());
        }
        ExpenseMeta expenseMeta = new ExpenseMeta(expenseName, description, paidBy, participants, contributions, amount, type);
        expenseController.addExpense(expenseMap, expenseMeta);
    }
    public void showBalanceForUser(Scanner scanner) {
        showUsers();
        logger.info("Enter user id for which you want to view details");
        String userId = scanner.next();
        expenseController.showBalanceForUser(expenseMap, userId);
    }
    public void showAllBalances() {
        expenseController.showAllBalances(expenseMap);
    }
}

package factory;

import lib.Constants;
import services.EqualSplitService;
import interfaces.Expense;
import services.PercentSplitService;

public class ExpenseFactory {
    public Expense getExpenseType(String type) {
        Expense expense;
        if (Constants.EQUAL.equals(type)) {
            expense = new EqualSplitService();
        } else if (Constants.PERCENT.equals(type)) {
            expense = new PercentSplitService();
        } else {
            expense = null;
        }
        return expense;
    }
}

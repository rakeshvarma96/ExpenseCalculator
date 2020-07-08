package services;

import interfaces.Expense;
import models.ExpenseMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PercentSplitService extends Expense {
    @Override
    public List<Double> getSplits(ExpenseMeta expenseMeta) {
        double amount = expenseMeta.getAmount();
        List<String> participants = expenseMeta.getParticipants();
        List<Double> contributions = expenseMeta.getContributions();
        List<Double> splits = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            double share = amount * contributions.get(i) / 100;
            splits.add(share);
        }
        return splits;
    }

    @Override
    public boolean addExpense(Map expenseMap, ExpenseMeta expenseMeta) {
        return super.addExpense(expenseMap, expenseMeta);
    }

    @Override
    public boolean isValidExpense(ExpenseMeta expenseMeta) {
        List<Double> contributions = expenseMeta.getContributions();
        double total = 0.00;
        for(double each:contributions)
            total += each;
        return total == 100.00;
    }
}

package services;

import interfaces.Expense;
import models.ExpenseMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EqualSplitService extends Expense {

    @Override
    public List<Double> getSplits(ExpenseMeta expenseMeta) {
        double amount = expenseMeta.getAmount();
        List<String> participants = expenseMeta.getParticipants();
        double share = amount / participants.size();
        List<Double> splits = new ArrayList<>();
        for (String ignored : participants)
            splits.add(share);
        return splits;
    }

    @Override
    public boolean addExpense(Map<String, Map<String, Double>> expenseMap, ExpenseMeta expenseMeta) {
        return super.addExpense(expenseMap, expenseMeta);
    }

    @Override
    public boolean isValidExpense(ExpenseMeta expenseMeta) {
        return super.isValidExpense(expenseMeta);
    }

}

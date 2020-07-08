package interfaces;

import models.ExpenseMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.List;

public abstract class Expense {
    private static final Logger logger = LoggerFactory.getLogger(Expense.class);
    public List<Double> getSplits(ExpenseMeta expenseMeta) {
        return null;
    }

    public boolean addExpense(Map expenseMap, ExpenseMeta expenseMeta) {
        if(!isValidExpense(expenseMeta)) {
            logger.error("Numbers don't add up");
            return false ;
        }
        Map<String, Double> balances;
        List<String> participants = expenseMeta.getParticipants();
        String paidBy = expenseMeta.getPaidBy();
        List<Double> splits = getSplits(expenseMeta);
        for(int i=0;i<participants.size();i++) {
            String paidTo = participants.get(i);
            if(paidBy.equals(paidTo))
                continue;
            double share = splits.get(i);
            balances = (Map<String, Double>) expenseMap.get(paidBy);
            balances.putIfAbsent(paidTo, 0.0);
            balances.put(paidTo, balances.get(paidTo)+share);
            balances = (Map<String, Double>)expenseMap.get(paidTo);
            balances.putIfAbsent(paidBy, 0.0);
            balances.put(paidBy, balances.get(paidBy)-share);
        }
        return true;
    }

    public boolean isValidExpense(ExpenseMeta expenseMeta) {
        return true;
    }
}

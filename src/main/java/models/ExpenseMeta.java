package models;

import java.util.List;

public class ExpenseMeta {
    private String expenseName;
    private String description;
    private String paidBy;
    private List<String> participants;
    private List<Double> contributions;
    private double amount;
    private String type;

    public ExpenseMeta() {
    }

    public ExpenseMeta(String expenseName, String notes, String paidBy, List<String> participants, List<Double> contributions, double amount, String type) {
        this.expenseName = expenseName;
        this.description = notes;
        this.paidBy = paidBy;
        this.participants = participants;
        this.contributions = contributions;
        this.amount = amount;
        this.type = type;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<Double> getContributions() {
        return contributions;
    }

    public void setContributions(List<Double> contributions) {
        this.contributions = contributions;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

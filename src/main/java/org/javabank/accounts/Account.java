package org.javabank.accounts;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private final String accountId;
    private final String pin;
    protected double balance;
    private final List<String> transactionHistory;

    public Account(String accountId, String pin, double initialBalance) {
        this.accountId = accountId;
        this.pin = pin;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposited: $" + amount);
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        balance -= amount;
        addTransaction("Withdrew: $" + amount);
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    protected void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public abstract void applyAccountSpecificRules();
}

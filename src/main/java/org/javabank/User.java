package org.javabank;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String username;
    private final String pin;
    private double balance;
    private final List<String> transactionHistory;

    public User(String username, String pin, double initialBalance) {
        this.username = username;
        this.pin = pin;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public String username() {
        return username;
    }

    public String pin() {
        return pin;
    }

    public double balance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }
}

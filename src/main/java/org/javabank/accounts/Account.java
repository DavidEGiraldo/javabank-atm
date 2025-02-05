package org.javabank.accounts;

import org.javabank.ex.InsufficientFundsException;
import org.javabank.ex.InvalidAccountException;
import org.javabank.utils.Authenticable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Account implements Authenticable {
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
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        addTransaction("Deposited: $" + amount);
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds. You only have $" + balance + " available.");
        }
        balance -= amount;
        addTransaction("Withdrew: $" + amount);
    }

    public void transfer(double amount, String targetAccountId, Map<String, Account> accounts) throws InsufficientFundsException, InvalidAccountException {
        if (!accounts.containsKey(targetAccountId)) {
            throw new InvalidAccountException("The target account ID '" + targetAccountId + "' does not exist.");
        }

        Account targetAccount = accounts.get(targetAccountId);

        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds. You only have $" + balance + " available.");
        }

        this.withdraw(amount);
        targetAccount.deposit(amount);
        addTransaction("Transferred: $" + amount + " to " + targetAccountId);
        targetAccount.addTransaction("Received: $" + amount + " from " + this.accountId);
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    protected void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    @Override
    public boolean authenticate(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public abstract void applyAccountSpecificRules();
}

package org.javabank.accounts;

import org.javabank.ex.InsufficientFundsException;
import org.javabank.ex.InvalidAccountException;
import org.javabank.utils.Authenticable;
import org.javabank.utils.TransactionLogger;

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
        deposit(amount, false);
    }

    private void deposit(double amount, boolean silent) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        if (!silent) logAndStoreTransaction("Deposited: $" + amount);
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        withdraw(amount, false);
    }

    private void withdraw(double amount, boolean silent) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds. You only have $" + balance + " available.");
        }
        balance -= amount;
        if (!silent) logAndStoreTransaction("Withdrew: $" + amount);
    }

    public void transfer(double amount, String targetAccountId, Map<String, Account> accounts) throws InsufficientFundsException, InvalidAccountException {
        if (!accounts.containsKey(targetAccountId)) {
            throw new InvalidAccountException("The target account ID '" + targetAccountId + "' does not exist.");
        }

        Account targetAccount = accounts.get(targetAccountId);

        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds. You only have $" + balance + " available.");
        }

        this.withdraw(amount, true);
        targetAccount.deposit(amount, true);

        logAndStoreTransaction("Transferred: $" + amount + " to " + targetAccountId);
        targetAccount.logAndStoreTransaction("Received: $" + amount + " from " + this.accountId);
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    private void logAndStoreTransaction(String transaction) {
        transactionHistory.add(transaction);
        TransactionLogger.logTransaction("[" + accountId + "] " + transaction);
    }

    @Override
    public boolean authenticate(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public abstract void applyAccountSpecificRules();
}

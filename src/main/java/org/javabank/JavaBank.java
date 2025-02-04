package org.javabank;

import org.javabank.accounts.Account;
import org.javabank.accounts.CheckingAccount;
import org.javabank.accounts.SavingsAccount;
import org.javabank.accounts.BusinessAccount;
import org.javabank.ex.InsufficientFundsException;
import org.javabank.utils.ConsoleUtils;
import org.javabank.utils.MessageType;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class JavaBank {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private final Map<String, Account> accounts;

    public JavaBank() {
        this.accounts = generateAccounts();
    }

    private Map<String, Account> generateAccounts() {
        Map<String, Account> accounts = new HashMap<>();
        accounts.put("checking1", new CheckingAccount("checking1", "1234", 1000.0));
        accounts.put("savings1", new SavingsAccount("savings1", "5678", 1500.0));
        accounts.put("business1", new BusinessAccount("business1", "0000", 5000.0));
        return accounts;
    }

    public void start() {
        ConsoleUtils.printMessage("Welcome to JavaBank ATM!", MessageType.INFO);
        String accountId = ConsoleUtils.promptForInput("Enter your account ID: ");
        Account account = accounts.get(accountId);

        if (account == null) {
            ConsoleUtils.printMessage("Account not found. Please try again.", MessageType.ERROR);
            return;
        }

        if (authenticateAccount(account)) {
            ConsoleUtils.printMessage("Authentication successful!", MessageType.SUCCESS);
            showAccountSummary(account);
            handleTransactions(account);
        } else {
            ConsoleUtils.printMessage("Too many failed attempts. Exiting...", MessageType.ERROR);
        }
    }

    private boolean authenticateAccount(Account account) {
        for (int i = 0; i < MAX_LOGIN_ATTEMPTS; i++) {
            String pin = ConsoleUtils.promptForInput("Enter PIN: ");
            if (account.authenticate(pin)) {
                return true;
            } else {
                ConsoleUtils.printMessage("Incorrect PIN. Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - i - 1), MessageType.WARNING);
            }
        }
        return false;
    }

    private void showAccountSummary(Account account) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        ConsoleUtils.printMessage("Account Summary:", MessageType.INFO);
        System.out.println("Account ID: " + account.getAccountId());
        System.out.println("Account Type: " + account.getClass().getSimpleName());
        System.out.println("Current Balance: $" + df.format(account.getBalance()));
    }

    private void handleTransactions(Account account) {
        boolean exit = false;
        while (!exit) {
            ConsoleUtils.printMessage("\n--- Main Menu ---", MessageType.INFO);
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Transactions");
            System.out.println("4. Exit");
            String choice = ConsoleUtils.promptForInput("Choose an option: ");

            switch (choice) {
                case "1" -> handleDeposit(account);
                case "2" -> handleWithdrawal(account);
                case "3" -> viewTransactions(account);
                case "4" -> {
                    ConsoleUtils.printMessage("Thank you for using JavaBank. Have a great day!", MessageType.SUCCESS);
                    exit = true;
                }
                default -> ConsoleUtils.printMessage("Invalid option. Please try again.", MessageType.WARNING);
            }
        }
    }

    private void handleDeposit(Account account) {
        String amountStr = ConsoleUtils.promptForInput("Enter deposit amount: ");
        try {
            double amount = Double.parseDouble(amountStr);
            account.deposit(amount);
            ConsoleUtils.printMessage("Deposit successful. New Balance: $" + formatBalance(account.getBalance()), MessageType.SUCCESS);
        } catch (NumberFormatException e) {
            ConsoleUtils.printMessage("Invalid input. Please enter a valid number.", MessageType.ERROR);
        } catch (IllegalArgumentException e) {
            ConsoleUtils.printMessage(e.getMessage(), MessageType.ERROR);
        }
    }

    private void handleWithdrawal(Account account) {
        String amountStr = ConsoleUtils.promptForInput("Enter withdrawal amount: ");
        try {
            double amount = Double.parseDouble(amountStr);
            account.withdraw(amount);
            ConsoleUtils.printMessage("Withdrawal successful. New Balance: $" + formatBalance(account.getBalance()), MessageType.SUCCESS);
        } catch (NumberFormatException e) {
            ConsoleUtils.printMessage("Invalid input. Please enter a valid number.", MessageType.ERROR);
        } catch (IllegalArgumentException | InsufficientFundsException e) {
            ConsoleUtils.printMessage(e.getMessage(), MessageType.ERROR);
        }
    }

    private void viewTransactions(Account account) {
        ConsoleUtils.printMessage("Transaction History:", MessageType.INFO);
        if (account.getTransactionHistory().isEmpty()) {
            ConsoleUtils.printMessage("No transactions available.", MessageType.INFO);
        } else {
            account.getTransactionHistory().forEach(System.out::println);
        }
    }

    private String formatBalance(double balance) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(balance);
    }
}

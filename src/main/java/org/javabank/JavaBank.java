package org.javabank;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class JavaBank {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final String SEPARATOR = "==============================";
    private final Map<String, User> users;
    private final Scanner scanner;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public enum MessageType {
        ERROR, WARNING, SUCCESS, INFO
    }

    public JavaBank() {
        this.users = generateUsers();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printWelcomeMessage();
        String username = promptForInput("Please enter your username: ");
        User user = users.get(username);

        if (user == null) {
            printMessage("User not found. Please try again.", MessageType.ERROR);
        } else {
            boolean isAuthenticated = authenticateUser(user);
            if (!isAuthenticated) {
                printMessage("Authentication failed! Too many attempts. Please try again later.", MessageType.ERROR);
            } else {
                printMessage("Authentication successful!", MessageType.SUCCESS);
                System.out.println(SEPARATOR);
                System.out.println("Welcome, " + user.username() + "!");
                System.out.println("Your current balance is: $" + user.balance());
                System.out.println(SEPARATOR);
                handleTransactions(user);
            }
        }
    }

    private Map<String, User> generateUsers() {
        Map<String, User> users = new HashMap<>();
        users.put("user1", new User("user1", "1234", 1000.0));
        users.put("user2", new User("user2", "5678", 1500.0));
        users.put("user3", new User("user3", "0000", 500.0));
        return users;
    }

    private boolean authenticateUser(User user) {
        for (int attempt = 1; attempt <= MAX_LOGIN_ATTEMPTS; attempt++) {
            String inputPin = promptForInput("Please enter your PIN (Attempt " + attempt + " of " + MAX_LOGIN_ATTEMPTS + "): ");
            if (user.pin().equals(inputPin)) {
                return true;
            } else {
                printMessage("Incorrect PIN.", MessageType.WARNING);
            }
        }
        return false;
    }

    private void handleTransactions(User user) {
        boolean exit = false;
        while (!exit) {
            System.out.println(SEPARATOR);
            System.out.println("Select an option:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Transaction History");
            System.out.println("4. Exit");
            System.out.println(SEPARATOR);

            String choice = promptForInput("Enter your choice: ");
            switch (choice) {
                case "1" -> handleDeposit(user);
                case "2" -> handleWithdrawal(user);
                case "3" -> viewTransactionHistory(user);
                case "4" -> {
                    printMessage("Thank you for using JavaBank ATM. Goodbye!", MessageType.INFO);
                    exit = true;
                }
                default -> printMessage("Invalid option. Please try again.", MessageType.WARNING);
            }
        }
    }

    private void handleDeposit(User user) {
        String amountStr = promptForInput("Enter the amount to deposit: ");
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                printMessage("Amount must be positive.", MessageType.ERROR);
            } else {
                user.deposit(amount);
                user.addTransaction("Deposited: $" + amount);
                printMessage("Deposit successful. New balance: $" + user.balance(), MessageType.SUCCESS);
            }
        } catch (NumberFormatException e) {
            printMessage("Invalid amount. Please enter a valid number.", MessageType.ERROR);
        }
    }

    private void handleWithdrawal(User user) {
        String amountStr = promptForInput("Enter the amount to withdraw: ");
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                printMessage("Amount must be positive.", MessageType.ERROR);
            } else if (amount > user.balance()) {
                printMessage("Insufficient balance for this withdrawal.", MessageType.ERROR);
            } else {
                user.withdraw(amount);
                user.addTransaction("Withdrew: $" + amount);
                printMessage("Withdrawal successful. New balance: $" + user.balance(), MessageType.SUCCESS);
            }
        } catch (NumberFormatException e) {
            printMessage("Invalid amount. Please enter a valid number.", MessageType.ERROR);
        }
    }

    private void viewTransactionHistory(User user) {
        System.out.println(SEPARATOR);
        System.out.println("Transaction History for " + user.username() + ":");
        if (user.getTransactionHistory().isEmpty()) {
            printMessage("No transactions found.", MessageType.INFO);
        } else {
            for (String transaction : user.getTransactionHistory()) {
                System.out.println(transaction);
            }
        }
        System.out.println(SEPARATOR);
    }

    private void printWelcomeMessage() {
        System.out.println(SEPARATOR);
        System.out.println(ANSI_BLUE + "Welcome to JavaBank ATM!" + ANSI_RESET);
        System.out.println(SEPARATOR);
    }

    private String promptForInput(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private void printMessage(String message, MessageType type) {
        String color;
        String prefix = switch (type) {
            case ERROR -> {
                color = ANSI_RED;
                yield "ERROR: ";
            }
            case WARNING -> {
                color = ANSI_YELLOW;
                yield "WARNING: ";
            }
            case SUCCESS -> {
                color = ANSI_GREEN;
                yield "SUCCESS: ";
            }
            default -> {
                color = ANSI_BLUE;
                yield "INFO: ";
            }
        };
        System.out.println(color + prefix + message + ANSI_RESET);
    }

    public static void main(String[] args) {
        new JavaBank().start();
    }
}

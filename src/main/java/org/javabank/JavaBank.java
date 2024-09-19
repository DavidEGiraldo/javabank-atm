package org.javabank;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JavaBank {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final String SEPARATOR = "==============================";
    private final List<User> users;
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
        User user = findUserByUsername(username);

        if (user == null) {
            printMessage("User not found. Please try again.", MessageType.ERROR);
        } else {
            boolean isAuthenticated = authenticateUser(user);
            if (!isAuthenticated) {
                printMessage("Authentication failed!. Too many attempts. Please try again later.", MessageType.ERROR);
            } else {
                printMessage("Authentication successful!", MessageType.SUCCESS);
                System.out.println(SEPARATOR);
                System.out.println("Welcome, " + user.getUsername() + "!");
            }
        }
    }

    private List<User> generateUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("user1", "1234"));
        users.add(new User("user2", "5678"));
        users.add(new User("user3", "0000"));
        return users;
    }

    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private boolean authenticateUser(User user) {
        for (int attempt = 1; attempt <= MAX_LOGIN_ATTEMPTS; attempt++) {
            String inputPin = promptForInput("Please enter your PIN (Attempt " + attempt + " of " + MAX_LOGIN_ATTEMPTS + "): ");
            if (user.getPin().equals(inputPin)) {
                return true;
            } else {
                printMessage("Incorrect PIN.", MessageType.WARNING);
            }
        }
        return false;
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
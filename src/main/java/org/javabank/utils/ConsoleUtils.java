package org.javabank.utils;

import java.util.Scanner;

public class ConsoleUtils {
    private static final Scanner scanner = new Scanner(System.in);

    public static String promptForInput(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public static void printMessage(String message, MessageType type) {
        String color = switch (type) {
            case ERROR -> "\u001B[31m";
            case WARNING -> "\u001B[33m";
            case SUCCESS -> "\u001B[32m";
            case INFO -> "\u001B[34m";
        };
        System.out.println(color + message + "\u001B[0m");
    }
}

package org.javabank.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TransactionLogger {
    private static final String DIRECTORY = "logs";
    private static final String FILE_NAME = DIRECTORY + "/transactions.log";

    public static void logTransaction(String transaction) {
        try {
            Files.createDirectories(Paths.get(DIRECTORY));

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(transaction);
                writer.newLine();
            }
        } catch (IOException e) {
            ConsoleUtils.printMessage("Error writing transaction log: " + e.getMessage(), MessageType.ERROR);
        }
    }

    public static List<String> readTransactions() {
        List<String> transactions = new ArrayList<>();

        if (!Files.exists(Paths.get(FILE_NAME))) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactions.add(line);
            }
        } catch (IOException e) {
            ConsoleUtils.printMessage("Error reading transaction log: " + e.getMessage(), MessageType.ERROR);
        }
        return transactions;
    }
}

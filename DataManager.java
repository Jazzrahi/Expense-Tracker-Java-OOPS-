

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String FILE_NAME = "transactions.txt";

    public static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
            return transactions; // Return empty list
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue; // Skip bad lines

                String type = parts[0];
                double amount = Double.parseDouble(parts[1]);
                LocalDate date = LocalDate.parse(parts[2]);
                String description = parts[3];
                String categoryOrSource = parts[4];

                // POLYMORPHISM USE
                
                if ("EXPENSE".equals(type)) {
                    transactions.add(new Expense(amount, date, description, categoryOrSource));
                } else if ("INCOME".equals(type)) {
                    transactions.add(new Income(amount, date, description, categoryOrSource));
                }
            }
        } catch (IOException | NumberFormatException | java.time.format.DateTimeParseException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }

        return transactions;
    }

  
    public static void saveTransactions(List<Transaction> transactions) {
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) { 
            for (Transaction tx : transactions) {
               
                writer.write(tx.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }
}
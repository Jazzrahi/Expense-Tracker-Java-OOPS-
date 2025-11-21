

import java.io.Serializable;
import java.time.LocalDate;


public abstract class Transaction implements Serializable {

   
    private double amount;
    private LocalDate date;
    private String description;

    public Transaction(double amount, LocalDate date, String description) {
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    
    public abstract String getType();

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f (%s)", 
            getType(), date.toString(), amount, description);
    }
    
    public abstract String toFileString();
}
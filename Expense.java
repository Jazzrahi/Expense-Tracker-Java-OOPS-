
import java.time.LocalDate;


public class Expense extends Transaction {  // Expense "extends" Transaction

    private String category;

    public Expense(double amount, LocalDate date, String description, String category) {
        super(amount, date, description);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    
    @Override
    public String getType() {
        return "Expense";
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f (%s) - Category: %s",
                getType(), getDate().toString(), getAmount(), getDescription(), category);
    }
    
    @Override
    public String toFileString() {
        return String.format("EXPENSE,%.2f,%s,%s,%s", 
            getAmount(), getDate().toString(), getDescription(), category);
    }
}
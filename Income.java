
import java.time.LocalDate;

// Income  "extends" Transaction.
public class Income extends Transaction {

    private String source;

    public Income(double amount, LocalDate date, String description, String source) {
        super(amount, date, description);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    
    @Override
    public String getType() {
        return "Income";
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f (%s) - Source: %s",
                getType(), getDate().toString(), getAmount(), getDescription(), source);
    }
    
    @Override
    public String toFileString() {
        return String.format("INCOME,%.2f,%s,%s,%s", 
            getAmount(), getDate().toString(), getDescription(), source);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel; 

public class ExpenseTracker extends JFrame {

    private List<Transaction> transactions;

    private JComboBox<String> typeComboBox;
    private JTextField amountField;
    private JTextField dateField;
    private JTextField descriptionField;
    private JTextField categorySourceField;
    private JButton addButton;
    private JButton resetButton; 
    
    private JTable displayTable;     
    private DefaultTableModel tableModel; 
    private JButton deleteButton;   
    private JButton editButton;     
    
    private JLabel summaryLabel;

    public ExpenseTracker() {
        setTitle("Simple Expense Tracker");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        transactions = DataManager.loadTransactions();

        initComponents();

        updateTransactionDisplay();
        updateSummary();
    }

    private void initComponents() {
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.setBackground(new Color(248, 248, 255)); 

        typeComboBox = new JComboBox<>(new String[]{"Expense", "Income"});
        amountField = new JTextField();
        dateField = new JTextField(LocalDate.now().toString()); 
        descriptionField = new JTextField();
        categorySourceField = new JTextField();
        
        addButton = new JButton("Add Transaction ✚");
        addButton.setBackground(new Color(34, 167, 240)); 
        addButton.setForeground(Color.WHITE); 
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.setFocusPainted(false);
        
        resetButton = new JButton("Reset All Data");
        resetButton.setBackground(new Color(231, 76, 60)); 
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        resetButton.setFocusPainted(false);


        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeComboBox);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Category (if Expense) / Source (if Income):"));
        inputPanel.add(categorySourceField);
        
        inputPanel.add(resetButton); 
        inputPanel.add(addButton);   


        String[] columnNames = {"Type", "Amount", "Date", "Description", "Category/Source"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        displayTable = new JTable(tableModel);
        displayTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        
        JScrollPane scrollPane = new JScrollPane(displayTable);
        
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        actionPanel.setBackground(new Color(240, 240, 240)); 
        
        deleteButton = new JButton("Delete Selected ❌");
        deleteButton.setBackground(new Color(192, 57, 43)); 
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        
        editButton = new JButton("Edit Selected ✏️");
        editButton.setBackground(new Color(52, 152, 219)); 
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        
        actionPanel.add(deleteButton);
        actionPanel.add(editButton);
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.NORTH);
        northPanel.add(actionPanel, BorderLayout.CENTER);


        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.setBackground(new Color(230, 240, 250)); 
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 10, 5, 10), 
            BorderFactory.createLineBorder(new Color(150, 150, 200), 1) 
        ));
        
        summaryLabel = new JLabel("Summary will appear here.");
        summaryLabel.setFont(new Font("Monospaced", Font.BOLD, 14)); 
        summaryPanel.add(summaryLabel);


        add(northPanel, BorderLayout.NORTH); 
        add(scrollPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addTransaction());
        resetButton.addActionListener(e -> resetBalance());
        
        deleteButton.addActionListener(e -> deleteTransaction()); 
        editButton.addActionListener(e -> editTransactionSetup()); 
    }

    private void addTransaction() {
        String type = (String) typeComboBox.getSelectedItem();
        String amountText = amountField.getText();
        String dateText = dateField.getText();
        String description = descriptionField.getText();
        String categorySource = categorySourceField.getText();

        
        double amount;
        LocalDate date;
        try {
            amount = Double.parseDouble(amountText);
            date = LocalDate.parse(dateText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date. Please use YYYY-MM-DD format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Transaction newTransaction;
        if ("Expense".equals(type)) {
            newTransaction = new Expense(amount, date, description, categorySource);
        } else {
            newTransaction = new Income(amount, date, description, categorySource);
        }

        transactions.add(newTransaction);
        DataManager.saveTransactions(transactions);

        updateTransactionDisplay();
        updateSummary();

        addButton.setText("Add Transaction ✚");
        addButton.setBackground(new Color(34, 167, 240)); 
        amountField.setText("");
        descriptionField.setText("");
        categorySourceField.setText("");
    }

    private void updateTransactionDisplay() {
        tableModel.setRowCount(0); 

        if (transactions.isEmpty()) {
            return;
        }
        
        for (Transaction tx : transactions) {
            String type = (tx instanceof Income) ? "Income" : "Expense";
            String categorySource;
            
            if (tx instanceof Income) {
                categorySource = ((Income) tx).getSource(); 
            } else {
                categorySource = ((Expense) tx).getCategory(); 
            }
                                    
            tableModel.addRow(new Object[]{
                type,
                String.format("%.2f", tx.getAmount()), 
                tx.getDate().toString(),
                tx.getDescription(),
                categorySource
            });
        }
    }

    private void updateSummary() {
        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction tx : transactions) {
            if (tx instanceof Income) {
                totalIncome += tx.getAmount();
            } else if (tx instanceof Expense) {
                totalExpense += tx.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;
        
        summaryLabel.setForeground(new Color(44, 62, 80)); 
        
        String balanceText = String.format("Balance: $%.2f", balance);
        
        String balanceColor;
        if (balance < 0) {
             balanceColor = String.format("<span style='color:rgb(192, 57, 43);'>%s</span>", balanceText); 
        } else {
             balanceColor = String.format("<span style='color:rgb(39, 174, 96);'>%s</span>", balanceText); 
        }
        
        summaryLabel.setText(String.format(
            "<html>Total Income: $%.2f  |  Total Expense: $%.2f  |  %s</html>",
            totalIncome, totalExpense, balanceColor
        ));
    }
    
    private void deleteTransaction() {
        int selectedRow = displayTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this transaction?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            transactions.remove(selectedRow);

            DataManager.saveTransactions(transactions);
            updateTransactionDisplay();
            updateSummary();
            JOptionPane.showMessageDialog(this, "Transaction deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void editTransactionSetup() {
        int selectedRow = displayTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }


        Transaction tx = transactions.get(selectedRow);

        if (tx instanceof Income) {
            typeComboBox.setSelectedItem("Income");
            categorySourceField.setText(((Income) tx).getSource());
        } else {
            typeComboBox.setSelectedItem("Expense");
            categorySourceField.setText(((Expense) tx).getCategory());
        }
        
        amountField.setText(String.valueOf(tx.getAmount()));
        dateField.setText(tx.getDate().toString());
        descriptionField.setText(tx.getDescription());
        
        addButton.setText("Save Changes");
        addButton.setBackground(new Color(255, 165, 0)); // Orange for Edit Mode

        JOptionPane.showMessageDialog(this, 
            "Fields loaded. Edit the data and click 'Save Changes' to update this entry.", 
            "Edit Mode", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    private void resetBalance() {
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete ALL transactions?\nThis action cannot be undone.", 
            "Confirm Data Reset", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            transactions.clear();
            DataManager.saveTransactions(transactions); 
            updateTransactionDisplay();
            updateSummary();
            
            addButton.setText("Add Transaction ✚");
            addButton.setBackground(new Color(34, 167, 240)); 
            
            JOptionPane.showMessageDialog(this, 
                "All transaction data has been successfully reset.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

// main method to run
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginPage();
            }
        });
    }
}
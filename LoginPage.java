
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame implements ActionListener {

   
    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "pass123";

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    
public LoginPage() {
    setTitle("User Login");
    setSize(350, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null); 
    

    try {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception e) {
        // Handle exception
    }
   

    initComponents();
    setVisible(true);
}

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Username Panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        userPanel.add(usernameField);

        // Password Panel
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        passPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        passPanel.add(passwordField);

        // Message Label (for errors or instructions)
        messageLabel = new JLabel("Enter credentials to continue.", SwingConstants.CENTER);
        messageLabel.setForeground(Color.BLUE);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        panel.add(userPanel);
        panel.add(passPanel);
        panel.add(messageLabel);
        panel.add(loginButton);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
                dispose();

                SwingUtilities.invokeLater(() -> {
                    new ExpenseTracker().setVisible(true);
                });

            } else {
                // If Login failed
                messageLabel.setText("Invalid Username or Password.");
                messageLabel.setForeground(Color.RED);
            }
        }
    }
}
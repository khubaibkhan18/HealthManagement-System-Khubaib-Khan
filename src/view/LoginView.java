package view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
    private JComboBox<String> roleCombo;
    private JTextField idField;
    private JButton btnLogin;
    
    public LoginView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
     // Application title at top 
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("Healthcare Management System");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, gbc);
        
        gbc.gridwidth = 1;
        
        //User selects their role
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("I am a:"), gbc);
        
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{
            "Patient", "GP", "Specialist", "Nurse", "Staff", "Admin"
        });
        add(roleCombo, gbc);
        
     //ID input field for authentication
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Enter ID:"), gbc);
        
        gbc.gridx = 1;
        idField = new JTextField(15);
        add(idField, gbc);
        
        //Main login button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        btnLogin = new JButton("Login");
        add(btnLogin, gbc);

        gbc.gridy = 4;
        JTextArea instructions = new JTextArea(
            "Sample IDs:\n" +
            "• Patient: P001\n" +
            "• GP: C001\n" +
            "• Specialist: C005\n" +
            "• Nurse: C009\n" +
            "• Staff: ST001\n" +
            "• Admin: ADM001"
        );
        instructions.setEditable(false);
        instructions.setBackground(getBackground());
        add(instructions, gbc);
    }
    
    public JButton getLoginButton() { return btnLogin; }
    public String getSelectedRole() { return (String) roleCombo.getSelectedItem(); }
    public String getUserId() { return idField.getText().trim(); }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }
}
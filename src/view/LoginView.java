package view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
    private JComboBox<String> roleCombo;
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton btnLogin;
    private JButton btnRegisterPatient;
    
    public LoginView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("Healthcare Management System Login");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, gbc);
        
        gbc.gridwidth = 1;
        
        // Role selection - UPDATED WITH ALL ROLES
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Role:"), gbc);
        
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{
            "Patient", "GP", "Specialist", "Nurse", "Staff", "Admin"
        });
        roleCombo.setPreferredSize(new Dimension(150, 25));
        add(roleCombo, gbc);
        
        // ID field
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("ID:"), gbc);
        
        gbc.gridx = 1;
        idField = new JTextField(15);
        idField.setToolTipText("Enter your ID (P001 for patient, C001 for clinician, ST001 for staff)");
        add(idField, gbc);
        
        // Password field
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setText("123"); // Default password for simplicity
        add(passwordField, gbc);
        
        // Login button
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(120, 35));
        add(btnLogin, gbc);
        
        // Register button (for patients only)
        gbc.gridy = 5;
        btnRegisterPatient = new JButton("Register as New Patient");
        btnRegisterPatient.setPreferredSize(new Dimension(180, 30));
        add(btnRegisterPatient, gbc);
        
        // Add instructions - UPDATED WITH ALL ROLES
        gbc.gridy = 6;
        JTextArea instructions = new JTextArea(
            "Demo Credentials (Password: 123 for all):\n" +
            "• Patient: P001\n" +
            "• GP: C001\n" + 
            "• Specialist: C005\n" +
            "• Nurse: C009\n" +
            "• Staff: ST001\n" +
            "• Admin: ADM001\n\n" +
            "Note: Password checking is disabled for demo."
        );
        instructions.setEditable(false);
        instructions.setBackground(getBackground());
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 11));
        add(instructions, gbc);
    }
    
    // Getters for controller
    public JButton getLoginButton() { return btnLogin; }
    public JButton getRegisterPatientButton() { return btnRegisterPatient; }
    public String getSelectedRole() { return ((String) roleCombo.getSelectedItem()).toLowerCase(); }
    public String getUserId() { return idField.getText().trim(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    
    // Clear form
    public void clearForm() {
        idField.setText("");
        passwordField.setText("123");
    }
    
    // Show error message
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Show success message
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Auto-fill for testing
    public void autoFill(String role, String id) {
        roleCombo.setSelectedItem(Character.toUpperCase(role.charAt(0)) + role.substring(1));
        idField.setText(id);
    }
}
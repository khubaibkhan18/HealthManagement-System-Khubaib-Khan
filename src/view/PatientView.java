package view;

import controller.PatientController;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientView extends JPanel {

    private PatientController controller;

    private JTable table;
    private DefaultTableModel tableModel;

    // 14 CSV-matching fields
    private JLabel lblAutoId;
    private JTextField txtFirstName, txtLastName, txtDob, txtNhs, txtGender;
    private JTextField txtPhone, txtEmail;
    private JTextField txtAddress, txtPostcode;
    private JTextField txtEmergencyName, txtEmergencyPhone;
    private JTextField txtRegistrationDate, txtGpSurgery;
    
    // Button references
    private JButton btnAdd;
    private JButton btnDelete;

    public PatientView() {

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ============================================================
        // TABLE (BOTTOM)
        // ============================================================
        tableModel = new DefaultTableModel(
                new Object[]{
                        "ID", "First Name", "Last Name", "DOB", "NHS",
                        "Gender", "Phone", "Email", "Address", "Postcode",
                        "Emergency Name", "Emergency Phone",
                        "Registration Date", "GP Surgery ID"
                }, 0
        );

        table = new JTable(tableModel);
        table.setRowHeight(22);
        
        // ============================================================
        // ADD TABLE SELECTION LISTENER
        // ============================================================
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedPatientIntoForm();
            }
        });

        add(new JScrollPane(table), BorderLayout.SOUTH);

        // ============================================================
        // FORM (CENTER) — 4 columns using GridBagLayout
        // ============================================================
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 15, 10, 15);
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Create fields
        lblAutoId = new JLabel("P001");

        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtDob = new JTextField();
        txtNhs = new JTextField();
        txtGender = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();
        txtPostcode = new JTextField();
        txtEmergencyName = new JTextField();
        txtEmergencyPhone = new JTextField();
        txtRegistrationDate = new JTextField();
        txtGpSurgery = new JTextField();

        // Row counter
        int row = 0;

        // ============================================================
        // ADD FORM ROWS (4 columns each)
        // ============================================================
        add4(form, gc, row++, "Patient ID:", lblAutoId, "First Name:", txtFirstName);
        add4(form, gc, row++, "Last Name:", txtLastName, "DOB (YYYY-MM-DD):", txtDob);
        add4(form, gc, row++, "NHS Number:", txtNhs, "Gender (M/F):", txtGender);
        add4(form, gc, row++, "Phone Number:", txtPhone, "Email:", txtEmail);
        add4(form, gc, row++, "Address:", txtAddress, "Postcode:", txtPostcode);
        add4(form, gc, row++, "Emergency Name:", txtEmergencyName,
                "Emergency Phone:", txtEmergencyPhone);
        add4(form, gc, row++, "Registration Date:", txtRegistrationDate,
                "GP Surgery ID:", txtGpSurgery);

        add(form, BorderLayout.CENTER);

        // ============================================================
        // BUTTONS (TOP)
        // ============================================================
        btnAdd = new JButton("Add Patient");
        btnDelete = new JButton("Delete Selected");

        btnAdd.addActionListener(e -> onAdd());
        btnDelete.addActionListener(e -> onDelete());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttons.add(btnAdd);
        buttons.add(btnDelete);

        add(buttons, BorderLayout.NORTH);
    }

    // =================================================================
    // Helper — Adds one ROW with 4 columns
    // =================================================================
    private void add4(JPanel panel, GridBagConstraints gc, int row,
                      String label1, JComponent field1,
                      String label2, JComponent field2) {

        gc.gridy = row;

        // Left label
        gc.gridx = 0; gc.weightx = 0.15;
        panel.add(new JLabel(label1), gc);

        // Left field
        gc.gridx = 1; gc.weightx = 0.35;
        panel.add(field1, gc);

        // Right label
        gc.gridx = 2; gc.weightx = 0.15;
        panel.add(new JLabel(label2), gc);

        // Right field
        gc.gridx = 3; gc.weightx = 0.35;
        panel.add(field2, gc);
    }

    // ============================================================
    // DISABLE EDITING FOR PATIENTS
    // ============================================================
    private void disableEditingForPatient() {
        // Hide all buttons for patients
        btnAdd.setVisible(false);
        btnDelete.setVisible(false);
        
        // Disable all form fields for patients (make read-only)
        txtFirstName.setEditable(false);
        txtLastName.setEditable(false);
        txtDob.setEditable(false);
        txtNhs.setEditable(false);
        txtGender.setEditable(false);
        txtPhone.setEditable(false);
        txtEmail.setEditable(false);
        txtAddress.setEditable(false);
        txtPostcode.setEditable(false);
        txtEmergencyName.setEditable(false);
        txtEmergencyPhone.setEditable(false);
        txtRegistrationDate.setEditable(false);
        txtGpSurgery.setEditable(false);
        
        // Hide the auto-ID label since patients can't add
        lblAutoId.setVisible(false);
    }
    
    // ============================================================
    // DISABLE EDITING FOR CLINICIANS
    // ============================================================
    private void disableEditingForClinician() {
        // Clinicians cannot add or delete patients
        btnAdd.setVisible(false);
        btnDelete.setVisible(false);
        
        // But they can view patient details (fields remain enabled for viewing)
        // We'll make them read-only so clinicians can view but not edit
        txtFirstName.setEditable(false);
        txtLastName.setEditable(false);
        txtDob.setEditable(false);
        txtNhs.setEditable(false);
        txtGender.setEditable(false);
        txtPhone.setEditable(false);
        txtEmail.setEditable(false);
        txtAddress.setEditable(false);
        txtPostcode.setEditable(false);
        txtEmergencyName.setEditable(false);
        txtEmergencyPhone.setEditable(false);
        txtRegistrationDate.setEditable(false);
        txtGpSurgery.setEditable(false);
    }

    // ============================================================
    // CONTROLLER LINK
    // ============================================================
    public void setController(PatientController controller) {
        this.controller = controller;
        // Check if user is patient and disable editing
        if (controller != null) {
            String role = controller.getCurrentUser().getRole();
            
            if (role.equals("patient")) {
                disableEditingForPatient();
            } else if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
                disableEditingForClinician();
            }
            // Staff/admin keep full access
        }
    }

    // ============================================================
    // SHOW PATIENTS
    // ============================================================
    public void showPatients(List<Patient> list) {
        tableModel.setRowCount(0);

        for (Patient p : list) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getFirstName(), p.getLastName(),
                    p.getDateOfBirth(), p.getNhsNumber(), p.getGender(),
                    p.getPhoneNumber(), p.getEmail(), p.getAddress(),
                    p.getPostcode(), p.getEmergencyContactName(),
                    p.getEmergencyContactPhone(), p.getRegistrationDate(),
                    p.getGpSurgeryId()
            });
        }
    }

    // ============================================================
    // LOAD SELECTED PATIENT INTO FORM
    // ============================================================
    private void loadSelectedPatientIntoForm() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow < 0) {
            return; // No row selected
        }
        
        // Get data from selected row
        String patientId = getTableValue(selectedRow, 0);
        String firstName = getTableValue(selectedRow, 1);
        String lastName = getTableValue(selectedRow, 2);
        String dob = getTableValue(selectedRow, 3);
        String nhs = getTableValue(selectedRow, 4);
        String gender = getTableValue(selectedRow, 5);
        String phone = getTableValue(selectedRow, 6);
        String email = getTableValue(selectedRow, 7);
        String address = getTableValue(selectedRow, 8);
        String postcode = getTableValue(selectedRow, 9);
        String emergencyName = getTableValue(selectedRow, 10);
        String emergencyPhone = getTableValue(selectedRow, 11);
        String registrationDate = getTableValue(selectedRow, 12);
        String gpSurgery = getTableValue(selectedRow, 13);
        
        // Populate form fields
        lblAutoId.setText(patientId);
        txtFirstName.setText(firstName);
        txtLastName.setText(lastName);
        txtDob.setText(dob);
        txtNhs.setText(nhs);
        txtGender.setText(gender);
        txtPhone.setText(phone);
        txtEmail.setText(email);
        txtAddress.setText(address);
        txtPostcode.setText(postcode);
        txtEmergencyName.setText(emergencyName);
        txtEmergencyPhone.setText(emergencyPhone);
        txtRegistrationDate.setText(registrationDate);
        txtGpSurgery.setText(gpSurgery);
    }
    
    private String getTableValue(int row, int column) {
        Object value = tableModel.getValueAt(row, column);
        return (value == null) ? "" : value.toString();
    }

    // ============================================================
    // ADD PATIENT
    // ============================================================
    private void onAdd() {
        if (controller == null) return;

        Patient p = new Patient(
                lblAutoId.getText(),
                txtFirstName.getText(),
                txtLastName.getText(),
                txtDob.getText(),
                txtNhs.getText(),
                txtGender.getText(),
                txtPhone.getText(),
                txtEmail.getText(),
                txtAddress.getText(),
                txtPostcode.getText(),
                txtEmergencyName.getText(),
                txtEmergencyPhone.getText(),
                txtRegistrationDate.getText(),
                txtGpSurgery.getText()
        );

        controller.addPatient(p);
        
        // Clear form after adding
        clearForm();
    }

    // ============================================================
    // CLEAR FORM
    // ============================================================
    private void clearForm() {
        // Generate new ID for next patient
        // You might want to get this from controller
        // For now, just clear the ID
        lblAutoId.setText("");
        
        // Clear all text fields
        txtFirstName.setText("");
        txtLastName.setText("");
        txtDob.setText("");
        txtNhs.setText("");
        txtGender.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtPostcode.setText("");
        txtEmergencyName.setText("");
        txtEmergencyPhone.setText("");
        txtRegistrationDate.setText("");
        txtGpSurgery.setText("");
    }

    // ============================================================
    // DELETE PATIENT
    // ============================================================
    private void onDelete() {
        if (controller == null) return;

        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a patient first!");
            return;
        }

        String id = tableModel.getValueAt(row, 0).toString();
        Patient p = controller.findById(id);

        if (p != null) {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Delete patient " + p.getFullName() + "?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deletePatient(p);
                clearForm(); // Clear form after deletion
            }
        }
    }
}
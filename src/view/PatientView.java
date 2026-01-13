package view;

import controller.PatientController;
import model.Patient;
import model.ValidationUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientView extends JPanel {

    private PatientController controller;

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblAutoId;
    private JTextField txtFirstName, txtLastName, txtDob, txtNhs, txtGender;
    private JTextField txtPhone, txtEmail;
    private JTextField txtAddress, txtPostcode;
    private JTextField txtEmergencyName, txtEmergencyPhone;
    private JTextField txtRegistrationDate, txtGpSurgery;
    //action buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;

    public PatientView() {

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //patient list table at bottom which shows all patient records
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
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedPatientIntoForm();
            }
        });

        add(new JScrollPane(table), BorderLayout.SOUTH);

        // Patient details
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 15, 10, 15);
        gc.fill = GridBagConstraints.HORIZONTAL;

    // Creating text fields 
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

        int row = 0;

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

 // the control buttons
        btnAdd = new JButton("Add Patient");
        btnUpdate = new JButton("Update Patient");
        btnDelete = new JButton("Delete Selected");

        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttons.add(btnAdd);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);

        add(buttons, BorderLayout.NORTH);
    }
    private void add4(JPanel panel, GridBagConstraints gc, int row,
                      String label1, JComponent field1,
                      String label2, JComponent field2) {

        gc.gridy = row;
        gc.gridx = 0; gc.weightx = 0.15;
        panel.add(new JLabel(label1), gc);
        gc.gridx = 1; gc.weightx = 0.35;
        panel.add(field1, gc);
        gc.gridx = 2; gc.weightx = 0.15;
        panel.add(new JLabel(label2), gc);

        gc.gridx = 3; gc.weightx = 0.35;
        panel.add(field2, gc);
    }

 //Patients can only view their own data, not edit
    private void disableEditingForPatient() {
        btnAdd.setVisible(false);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
        
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
        
        lblAutoId.setVisible(false);
    }
    
    //Clinicians can view patient details but not modify them
    private void disableEditingForClinician() {
        btnAdd.setVisible(false);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
        
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

  //Set controller
    public void setController(PatientController controller) {
        this.controller = controller;
        if (controller != null) {
            String role = controller.getCurrentUser().getRole();
            
            if (role.equals("patient")) {
                disableEditingForPatient();
            } else if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
                disableEditingForClinician();
            } else if (role.equals("staff") || role.equals("admin")) {
              // Staffand admin editing access
                txtFirstName.setEditable(true);
                txtLastName.setEditable(true);
                txtDob.setEditable(true);
                txtNhs.setEditable(true);
                txtGender.setEditable(true);
                txtPhone.setEditable(true);
                txtEmail.setEditable(true);
                txtAddress.setEditable(true);
                txtPostcode.setEditable(true);
                txtEmergencyName.setEditable(true);
                txtEmergencyPhone.setEditable(true);
                txtRegistrationDate.setEditable(true);
                txtGpSurgery.setEditable(true);
                
                btnAdd.setVisible(true);
                btnUpdate.setVisible(true);
                btnDelete.setVisible(true);
                lblAutoId.setVisible(true);
            }
        }
    }
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

    private void loadSelectedPatientIntoForm() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow < 0) {
            return;
        }
        
        lblAutoId.setText(getTableValue(selectedRow, 0));
        txtFirstName.setText(getTableValue(selectedRow, 1));
        txtLastName.setText(getTableValue(selectedRow, 2));
        txtDob.setText(getTableValue(selectedRow, 3));
        txtNhs.setText(getTableValue(selectedRow, 4));
        txtGender.setText(getTableValue(selectedRow, 5));
        txtPhone.setText(getTableValue(selectedRow, 6));
        txtEmail.setText(getTableValue(selectedRow, 7));
        txtAddress.setText(getTableValue(selectedRow, 8));
        txtPostcode.setText(getTableValue(selectedRow, 9));
        txtEmergencyName.setText(getTableValue(selectedRow, 10));
        txtEmergencyPhone.setText(getTableValue(selectedRow, 11));
        txtRegistrationDate.setText(getTableValue(selectedRow, 12));
        txtGpSurgery.setText(getTableValue(selectedRow, 13));
    }
    
    private String getTableValue(int row, int column) {
        Object value = tableModel.getValueAt(row, column);
        return (value == null) ? "" : value.toString();
    }
    //addding a patient 
    private void onAdd() {
        if (controller == null) return;

        StringBuilder errors = new StringBuilder();
        
     // Validate the required fields 
        if (txtFirstName.getText().trim().isEmpty()) {
            errors.append("- First name is required\n");
        }
        if (txtLastName.getText().trim().isEmpty()) {
            errors.append("- Last name is required\n");
        }
        
        if (!ValidationUtils.isValidDate(txtDob.getText(), "yyyy-MM-dd")) {
            errors.append("- Date of birth must be in YYYY-MM-DD format\n");
        }
        
        if (!ValidationUtils.isValidNhsNumber(txtNhs.getText())) {
            errors.append("- NHS number must be 10 digits\n");
        }
        
        if (!ValidationUtils.isValidPhone(txtPhone.getText())) {
            errors.append("- Phone must start with 07 and be 11 digits\n");
        }
        
        if (!ValidationUtils.isValidEmail(txtEmail.getText())) {
            errors.append("- Invalid email format\n");
        }
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, 
                "Please fix the following errors:\n\n" + errors.toString(),
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
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
        clearForm();
    }

    private void clearForm() {
        lblAutoId.setText("");
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
                clearForm();
            }
        }
    }

    //update existing patient
    private void onUpdate() {
        if (controller == null) return;
        String patientId = lblAutoId.getText();
        if (patientId.isEmpty()) return;
        
        Patient p = new Patient(
            patientId, txtFirstName.getText(), txtLastName.getText(),
            txtDob.getText(), txtNhs.getText(), txtGender.getText(),
            txtPhone.getText(), txtEmail.getText(), txtAddress.getText(),
            txtPostcode.getText(), txtEmergencyName.getText(),
            txtEmergencyPhone.getText(), txtRegistrationDate.getText(),
            txtGpSurgery.getText()
        );
        
        controller.updatePatient(p);
    }
}
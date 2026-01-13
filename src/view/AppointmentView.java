package view;

import controller.AppointmentController;
import model.Appointment;
import model.ValidationUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentView extends JPanel {

    private AppointmentController controller;

    private JTable table;
    private DefaultTableModel model;

    // Form components
    private JTextField txtId, txtDate, txtTime, txtDuration, txtType;
    private JTextField txtReason, txtCreatedDate, txtLastModified;

    private JComboBox<String> cbStatus;
    private JComboBox<String> cbPatientId;
    private JComboBox<String> cbClinicianId;
    private JComboBox<String> cbFacilityId;

    private JTextArea txtNotes;

    // Button references
    private JButton btnUpdate;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AppointmentView() {
        setLayout(new BorderLayout(10, 10));

        // ============================================================
        // TABLE
        // ============================================================
        model = new DefaultTableModel(
                new Object[]{
                        "ID", "Patient", "Clinician", "Facility",
                        "Date", "Time", "Duration (min)", "Type",
                        "Status", "Reason", "Notes", "Created", "Last Modified"
                }, 0
        );

        table = new JTable(model);
        table.setRowHeight(22);
        
        // ============================================================
        // ADD TABLE SELECTION LISTENER FOR APPOINTMENTS
        // ============================================================
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedAppointmentIntoForm();
            }
        });

        add(new JScrollPane(table), BorderLayout.SOUTH);

        // ============================================================
        // FORM
        // ============================================================
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(); 
        txtId.setEditable(false);

        cbPatientId = new JComboBox<>();
        cbClinicianId = new JComboBox<>();
        cbFacilityId = new JComboBox<>();

        txtDate = new JTextField();
        txtTime = new JTextField();
        txtDuration = new JTextField();
        txtType = new JTextField();
        txtReason = new JTextField();

        cbStatus = new JComboBox<>(new String[]{
                "SCHEDULED",
                "RESCHEDULED",
                "CANCELLED",
                "COMPLETED",
                "NO-SHOW"
        });
        cbStatus.setFont(new Font("SansSerif", Font.PLAIN, 12));

        txtNotes = new JTextArea(3, 15);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);

        txtCreatedDate = new JTextField();
        txtLastModified = new JTextField();

        int row = 0;
        addFieldPair(form, gc, row++, "Appointment ID:", txtId, "Patient ID:", cbPatientId);
        addFieldPair(form, gc, row++, "Clinician ID:", cbClinicianId, "Facility ID:", cbFacilityId);
        addFieldPair(form, gc, row++, "Appointment Date:", txtDate, "Time (HH:mm):", txtTime);
        addFieldPair(form, gc, row++, "Duration (min):", txtDuration, "Appointment Type:", txtType);
        addFieldPair(form, gc, row++, "Status:", cbStatus, "Reason for Visit:", txtReason);
        addFieldPair(form, gc, row++, "Created Date:", txtCreatedDate, "Last Modified:", txtLastModified);

        // Notes row
        gc.gridx = 0; gc.gridy = row; gc.gridwidth = 1;
        form.add(new JLabel("Notes:"), gc);

        gc.gridx = 1; gc.gridy = row; gc.gridwidth = 3;
        form.add(new JScrollPane(txtNotes), gc);

        add(form, BorderLayout.CENTER);

        // ============================================================
        // BUTTONS
        // ============================================================
        JButton btnAdd = new JButton("Add Appointment");
        btnUpdate = new JButton("Update Appointment");
        JButton btnDelete = new JButton("Delete Selected");

        btnAdd.addActionListener(e -> addAppointment());
        btnUpdate.addActionListener(e -> updateAppointment());
        btnDelete.addActionListener(e -> deleteAppointment());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttons.add(btnAdd);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);

        add(buttons, BorderLayout.NORTH);
    }

    // Helper method for form layout
    private void addFieldPair(JPanel panel, GridBagConstraints gc, int row,
                              String l1, JComponent f1,
                              String l2, JComponent f2) {

        gc.gridwidth = 1;

        gc.gridx = 0; gc.gridy = row;
        panel.add(new JLabel(l1), gc);

        gc.gridx = 1;
        panel.add(f1, gc);

        gc.gridx = 2;
        panel.add(new JLabel(l2), gc);

        gc.gridx = 3;
        panel.add(f2, gc);
    }

    // ============================================================
    // Dropdown loading
    // ============================================================
    public void loadDropdowns(List<String> patients, List<String> clinicians, List<String> facilities) {
        cbPatientId.removeAllItems();
        cbClinicianId.removeAllItems();
        cbFacilityId.removeAllItems();

        // If controller exists and user is a patient, set patient dropdown to their ID only
        if (controller != null && controller.getCurrentUser().getRole().equals("patient")) {
            String patientId = controller.getCurrentUser().getId();
            cbPatientId.addItem(patientId);
            cbPatientId.setEnabled(false); // Patient can't change their ID
        } else {
            // Load all patients for other roles
            for (String s : patients) cbPatientId.addItem(s);
        }

        // Load clinicians and facilities normally
        for (String s : clinicians) cbClinicianId.addItem(s);
        for (String s : facilities) cbFacilityId.addItem(s);

        txtId.setText(controller.generateId());
        txtCreatedDate.setText(LocalDate.now().format(fmt));
        txtLastModified.setText(LocalDate.now().format(fmt));
    }

    // ============================================================
    // TABLE VIEW UPDATE
    // ============================================================
    public void showAppointments(List<Appointment> list) {
        model.setRowCount(0);

        for (Appointment a : list) {
            model.addRow(new Object[]{
                    a.getId(),
                    a.getPatientId(),
                    a.getClinicianId(),
                    a.getFacilityId(),
                    a.getAppointmentDate(),
                    a.getAppointmentTime(),
                    a.getDurationMinutes(),
                    a.getAppointmentType(),
                    a.getStatus(),
                    a.getReasonForVisit(),
                    a.getNotes(),
                    a.getCreatedDate(),
                    a.getLastModified()
            });
        }
    }

    // ============================================================
    // LOAD SELECTED APPOINTMENT INTO FORM
    // ============================================================
    private void loadSelectedAppointmentIntoForm() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow < 0) {
            return; // No row selected
        }
        
        // Get data from selected row
        txtId.setText(getTableValue(selectedRow, 0));
        setComboBoxValue(cbPatientId, getTableValue(selectedRow, 1));
        setComboBoxValue(cbClinicianId, getTableValue(selectedRow, 2));
        setComboBoxValue(cbFacilityId, getTableValue(selectedRow, 3));
        txtDate.setText(getTableValue(selectedRow, 4));
        txtTime.setText(getTableValue(selectedRow, 5));
        txtDuration.setText(getTableValue(selectedRow, 6));
        txtType.setText(getTableValue(selectedRow, 7));
        setComboBoxValue(cbStatus, getTableValue(selectedRow, 8));
        txtReason.setText(getTableValue(selectedRow, 9));
        txtNotes.setText(getTableValue(selectedRow, 10));
        txtCreatedDate.setText(getTableValue(selectedRow, 11));
        txtLastModified.setText(getTableValue(selectedRow, 12));
    }
    
    private String getTableValue(int row, int column) {
        Object value = model.getValueAt(row, column);
        return (value == null) ? "" : value.toString();
    }
    
    private void setComboBoxValue(JComboBox<String> comboBox, String value) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(value)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        // If value not found, add it and select it
        if (!value.isEmpty()) {
            comboBox.addItem(value);
            comboBox.setSelectedItem(value);
        }
    }

    // ============================================================
    // ADD APPOINTMENT WITH VALIDATION
    // ============================================================
    private void addAppointment() {
        // Validate required fields
        if (cbPatientId.getSelectedItem() == null || cbPatientId.getSelectedItem().toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient ID is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (cbClinicianId.getSelectedItem() == null || cbClinicianId.getSelectedItem().toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Clinician ID is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (txtDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Appointment date is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if patient is trying to add appointment for someone else
        if (controller.getCurrentUser().getRole().equals("patient")) {
            String selectedPatientId = (String) cbPatientId.getSelectedItem();
            String currentPatientId = controller.getCurrentUser().getId();
            
            if (!selectedPatientId.equals(currentPatientId)) {
                JOptionPane.showMessageDialog(this, 
                    "You can only create appointments for yourself.", 
                    "Permission Denied", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // ADDED VALIDATION CHECKS
        StringBuilder errors = new StringBuilder();
        
        // Date validation
        if (!ValidationUtils.isValidDate(txtDate.getText(), "yyyy-MM-dd") &&
            !ValidationUtils.isValidDate(txtDate.getText(), "dd/MM/yyyy")) {
            errors.append("- Appointment date must be in YYYY-MM-DD or DD/MM/YYYY format\n");
        }
        
        // Time validation (HH:mm format)
        String time = txtTime.getText().trim();
        if (!time.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            errors.append("- Time must be in HH:MM format (e.g., 09:30)\n");
        }
        
        // Duration validation
        if (!ValidationUtils.isValidNumber(txtDuration.getText())) {
            errors.append("- Duration must be a number\n");
        }
        
        // Show errors if any
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, 
                "Please fix the following errors:\n\n" + errors.toString(),
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Appointment a = new Appointment(
                txtId.getText(),
                (String) cbPatientId.getSelectedItem(),
                (String) cbClinicianId.getSelectedItem(),
                (String) cbFacilityId.getSelectedItem(),
                txtDate.getText(),
                txtTime.getText(),
                txtDuration.getText(),
                txtType.getText(),
                (String) cbStatus.getSelectedItem(),
                txtReason.getText(),
                txtNotes.getText(),
                txtCreatedDate.getText(),
                txtLastModified.getText()
        );

        controller.addAppointment(a);

        // Clear form and regenerate ID
        clearAppointmentForm();
    }

    // ============================================================
    // CLEAR APPOINTMENT FORM
    // ============================================================
    private void clearAppointmentForm() {
        // Regenerate ID & timestamps
        txtId.setText(controller.generateId());
        txtLastModified.setText(LocalDate.now().format(fmt));
        
        // Clear form fields (except dropdowns)
        txtDate.setText("");
        txtTime.setText("");
        txtDuration.setText("");
        txtType.setText("");
        txtReason.setText("");
        txtNotes.setText("");
    }

    // ============================================================
    // DELETE APPOINTMENT
    // ============================================================
    private void deleteAppointment() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an appointment to delete.");
            return;
        }

        String id = model.getValueAt(row, 0).toString();
        
        // Check if patient can delete this appointment
        if (controller.getCurrentUser().getRole().equals("patient")) {
            String patientIdInTable = model.getValueAt(row, 1).toString();
            String currentPatientId = controller.getCurrentUser().getId();
            
            if (!patientIdInTable.equals(currentPatientId)) {
                JOptionPane.showMessageDialog(this, 
                    "You can only delete your own appointments.", 
                    "Permission Denied", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Delete appointment " + id + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteById(id);
            clearAppointmentForm(); // Clear form after deletion
        }
    }

    // ============================================================
    // UPDATE APPOINTMENT
    // ============================================================
    private void updateAppointment() {
        if (controller == null) return;
        
        String appointmentId = txtId.getText();
        if (appointmentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select an appointment to update.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate required fields
        if (cbPatientId.getSelectedItem() == null || cbPatientId.getSelectedItem().toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient ID is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (cbClinicianId.getSelectedItem() == null || cbClinicianId.getSelectedItem().toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Clinician ID is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (txtDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Appointment date is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Appointment a = new Appointment(
            appointmentId,
            (String) cbPatientId.getSelectedItem(),
            (String) cbClinicianId.getSelectedItem(),
            (String) cbFacilityId.getSelectedItem(),
            txtDate.getText(),
            txtTime.getText(),
            txtDuration.getText(),
            txtType.getText(),
            (String) cbStatus.getSelectedItem(),
            txtReason.getText(),
            txtNotes.getText(),
            txtCreatedDate.getText(),
            txtLastModified.getText()
        );
        
        controller.updateAppointment(a);
        clearAppointmentForm();
    }

    // ============================================================
    // SET CONTROLLER WITH ROLE-BASED PERMISSIONS
    // ============================================================
    public void setController(AppointmentController controller) {
        this.controller = controller;
        if (controller != null) {
            String role = controller.getCurrentUser().getRole();
            
            // Patients can't edit appointment details
            if (role.equals("patient")) {
                cbPatientId.setEnabled(false);
                cbClinicianId.setEnabled(false);
                cbFacilityId.setEnabled(false);
                txtDate.setEditable(false);
                txtTime.setEditable(false);
                txtDuration.setEditable(false);
                txtType.setEditable(false);
                cbStatus.setEnabled(false);
                txtReason.setEditable(false);
                txtNotes.setEditable(false);
            }
            // Staff, clinicians, admin can edit all fields
        }
    }
}
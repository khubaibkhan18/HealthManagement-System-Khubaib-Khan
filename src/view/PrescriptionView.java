package view;

import controller.PrescriptionController;
import model.Prescription;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PrescriptionView extends JPanel {

    private PrescriptionController controller;
    private JTable table;
    private DefaultTableModel model;

    // Form fields
    private JLabel lblId;
    private JComboBox<String> cbPatientId, cbClinicianId, cbDrug, cbPharmacy, cbStatus, cbAppointmentId;
    private JTextField txtPrescDate, txtDosage, txtFrequency, txtDuration, txtQuantity, txtIssueDate, txtCollectionDate;
    private JTextArea txtInstructions;
    private JButton btnAdd, btnUpdate, btnDelete;

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private final SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

    public PrescriptionView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table showing prescription list
        model = new DefaultTableModel(
                new Object[]{"ID", "Patient", "Clinician", "Appt", "Presc Date", "Drug", "Dosage", 
                           "Freq", "Duration", "Qty", "Instructions", "Pharmacy", "Status", "Issue", "Collected"}, 0);
        table = new JTable(model);
        table.setRowHeight(22);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.EAST);

     //Buttons at top
        JPanel btnPanel = createButtonPanel();
        add(btnPanel, BorderLayout.NORTH);

        // Load selected row into form when clicked
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedRowIntoForm();
        });
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Prescription Details"));
        panel.setPreferredSize(new Dimension(400, 600));

        lblId = new JLabel("RX001");

        cbPatientId = new JComboBox<>();
        cbClinicianId = new JComboBox<>();
        cbDrug = new JComboBox<>();
        cbPharmacy = new JComboBox<>();
        cbAppointmentId = new JComboBox<>();
        cbStatus = new JComboBox<>(new String[]{"PENDING", "ISSUED", "COLLECTED", "CANCELLED", "REJECTED"});

        txtPrescDate = new JTextField();
        txtDosage = new JTextField();
        txtFrequency = new JTextField();
        txtDuration = new JTextField();
        txtQuantity = new JTextField();
        txtIssueDate = new JTextField();
        txtCollectionDate = new JTextField();
        txtInstructions = new JTextArea(3, 15);
        txtInstructions.setLineWrap(true);

        // Add form components in pairs
        panel.add(new JLabel("Prescription ID:"));
        panel.add(lblId);
        
        panel.add(new JLabel("Patient ID:"));
        panel.add(cbPatientId);
        
        panel.add(new JLabel("Clinician ID:"));
        panel.add(cbClinicianId);
        
        panel.add(new JLabel("Appointment ID:"));
        panel.add(cbAppointmentId);
        
        panel.add(new JLabel("Prescription Date:"));
        panel.add(txtPrescDate);
        
        panel.add(new JLabel("Drug:"));
        panel.add(cbDrug);
        
        panel.add(new JLabel("Dosage:"));
        panel.add(txtDosage);
        
        panel.add(new JLabel("Frequency:"));
        panel.add(txtFrequency);
        
        panel.add(new JLabel("Duration (days):"));
        panel.add(txtDuration);
        
        panel.add(new JLabel("Quantity:"));
        panel.add(txtQuantity);
        
        panel.add(new JLabel("Pharmacy:"));
        panel.add(cbPharmacy);
        
        panel.add(new JLabel("Status:"));
        panel.add(cbStatus);
        
        panel.add(new JLabel("Issue Date:"));
        panel.add(txtIssueDate);
        
        panel.add(new JLabel("Collection Date:"));
        panel.add(txtCollectionDate);
        
        panel.add(new JLabel("Instructions:"));
        panel.add(new JScrollPane(txtInstructions));

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        
        return panel;
    }

    // Disable editing for patients 
    private void disableEditingForPatient() {
        btnAdd.setVisible(false);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
        
        cbPatientId.setEnabled(false);
        cbClinicianId.setEnabled(false);
        cbDrug.setEnabled(false);
        cbPharmacy.setEnabled(false);
        cbStatus.setEnabled(false);
        cbAppointmentId.setEnabled(false);
        
        txtPrescDate.setEditable(false);
        txtDosage.setEditable(false);
        txtFrequency.setEditable(false);
        txtDuration.setEditable(false);
        txtQuantity.setEditable(false);
        txtIssueDate.setEditable(false);
        txtCollectionDate.setEditable(false);
        txtInstructions.setEditable(false);
        
        lblId.setVisible(false);
    }

    // Disable editing for staff
    private void disableEditingForStaff() {
        btnAdd.setVisible(false);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
        
        cbPatientId.setEnabled(false);
        cbClinicianId.setEnabled(false);
        cbDrug.setEnabled(false);
        cbPharmacy.setEnabled(false);
        cbStatus.setEnabled(false);
        cbAppointmentId.setEnabled(false);
        
        txtPrescDate.setEditable(false);
        txtDosage.setEditable(false);
        txtFrequency.setEditable(false);
        txtDuration.setEditable(false);
        txtQuantity.setEditable(false);
        txtIssueDate.setEditable(false);
        txtCollectionDate.setEditable(false);
        txtInstructions.setEditable(false);
        
        lblId.setVisible(false);
    }

    public void setController(PrescriptionController controller) {
        this.controller = controller;
        if (controller != null) {
            String role = controller.getCurrentUser().getRole();
            if (role.equals("patient")) {
                disableEditingForPatient();
            } else if (role.equals("staff")) {
                disableEditingForStaff();
            }
        }
    }

    public void populateDropdowns(List<String> patientIds, List<String> clinicianIds,
                                  List<String> drugs, List<String> pharmacies, List<String> appointmentIds) {
        cbPatientId.removeAllItems();
        for (String id : patientIds) cbPatientId.addItem(id);

        cbClinicianId.removeAllItems();
        for (String id : clinicianIds) cbClinicianId.addItem(id);

        cbDrug.removeAllItems();
        for (String d : drugs) cbDrug.addItem(d);

        cbPharmacy.removeAllItems();
        for (String ph : pharmacies) cbPharmacy.addItem(ph);

        cbAppointmentId.removeAllItems();
        for (String ap : appointmentIds) cbAppointmentId.addItem(ap);
    }

    public void setNextId(String id) {
        lblId.setText(id);
    }

    public void showPrescriptions(List<Prescription> list) {
        model.setRowCount(0);
        for (Prescription p : list) {
            model.addRow(new Object[]{
                p.getId(), p.getPatientId(), p.getClinicianId(), p.getAppointmentId(),
                p.getPrescriptionDate(), p.getMedication(), p.getDosage(), p.getFrequency(),
                p.getDurationDays(), p.getQuantity(), p.getInstructions(), p.getPharmacyName(),
                p.getStatus(), p.getIssueDate(), p.getCollectionDate()
            });
        }
    }

    private void onAdd() {
        if (controller == null) return;
        String errors = validateForm();
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, errors, "Validation error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Prescription p = buildFromForm(lblId.getText());
        controller.addPrescription(p);
        clearForm();
    }

    private void onUpdate() {
        if (controller == null) return;
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to update.");
            return;
        }
        String errors = validateForm();
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, errors, "Validation error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String id = lblId.getText();
        Prescription p = buildFromForm(id);
        controller.updatePrescription(p);
    }

    private void onDelete() {
        if (controller == null) return;
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }
        String id = model.getValueAt(row, 0).toString();
        controller.deleteById(id);
    }

    private Prescription buildFromForm(String id) {
        return new Prescription(
            id,
            (String) cbPatientId.getSelectedItem(),
            (String) cbClinicianId.getSelectedItem(),
            (String) cbAppointmentId.getSelectedItem(),
            txtPrescDate.getText().trim(),
            (String) cbDrug.getSelectedItem(),
            txtDosage.getText().trim(),
            txtFrequency.getText().trim(),
            txtDuration.getText().trim(),
            txtQuantity.getText().trim(),
            txtInstructions.getText().trim(),
            (String) cbPharmacy.getSelectedItem(),
            (String) cbStatus.getSelectedItem(),
            txtIssueDate.getText().trim(),
            txtCollectionDate.getText().trim()
        );
    }

    private void loadSelectedRowIntoForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        lblId.setText(model.getValueAt(row, 0).toString());
        cbPatientId.setSelectedItem(model.getValueAt(row, 1));
        cbClinicianId.setSelectedItem(model.getValueAt(row, 2));
        cbAppointmentId.setSelectedItem(model.getValueAt(row, 3));
        txtPrescDate.setText(value(row, 4));
        cbDrug.setSelectedItem(model.getValueAt(row, 5));
        txtDosage.setText(value(row, 6));
        txtFrequency.setText(value(row, 7));
        txtDuration.setText(value(row, 8));
        txtQuantity.setText(value(row, 9));
        txtInstructions.setText(value(row, 10));
        cbPharmacy.setSelectedItem(model.getValueAt(row, 11));
        cbStatus.setSelectedItem(model.getValueAt(row, 12));
        txtIssueDate.setText(value(row, 13));
        txtCollectionDate.setText(value(row, 14));
    }

    private String value(int row, int col) {
        Object v = model.getValueAt(row, col);
        return v == null ? "" : v.toString();
    }

    private String validateForm() {
        StringBuilder sb = new StringBuilder();
        if (cbPatientId.getSelectedItem() == null) sb.append("- Patient ID is required.\n");
        if (cbClinicianId.getSelectedItem() == null) sb.append("- Clinician ID is required.\n");
        if (cbDrug.getSelectedItem() == null) sb.append("- Drug must be selected.\n");
        if (txtDosage.getText().trim().isEmpty()) sb.append("- Dosage is required.\n");
        
        if (!txtDuration.getText().trim().isEmpty()) {
            try { Integer.parseInt(txtDuration.getText().trim()); }
            catch (NumberFormatException e) { sb.append("- Duration must be a number.\n"); }
        }
        
        if (!txtQuantity.getText().trim().isEmpty()) {
            try { Integer.parseInt(txtQuantity.getText().trim()); }
            catch (NumberFormatException e) { sb.append("- Quantity must be a number.\n"); }
        }
        
        checkDate(txtPrescDate.getText().trim(), "Prescription Date", sb);
        if (!txtIssueDate.getText().trim().isEmpty()) checkDate(txtIssueDate.getText().trim(), "Issue Date", sb);
        if (!txtCollectionDate.getText().trim().isEmpty()) checkDate(txtCollectionDate.getText().trim(), "Collection Date", sb);
        
        return sb.toString();
    }

    private void checkDate(String value, String label, StringBuilder sb) {
        if (value.isEmpty()) return;
        sdf.setLenient(false);
        try { sdf.parse(value); }
        catch (ParseException e) {
            sb.append("- ").append(label).append(" must be in format ").append(DATE_PATTERN).append(".\n");
        }
    }

    private void clearForm() {
        txtPrescDate.setText("");
        txtDosage.setText("");
        txtFrequency.setText("");
        txtDuration.setText("");
        txtQuantity.setText("");
        txtIssueDate.setText("");
        txtCollectionDate.setText("");
        txtInstructions.setText("");
        cbStatus.setSelectedIndex(0);
    }
}
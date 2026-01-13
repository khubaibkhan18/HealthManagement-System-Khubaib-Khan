package view;

import controller.ReferralController;
import model.Referral;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReferralView extends JPanel {

    private ReferralController controller;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtId, txtReason, txtRequestedService, txtCreatedDate, txtLastUpdated;
    private JTextArea txtClinicalSummary, txtNotes;
    private JFormattedTextField txtReferralDate;
    //Dropdown
    private JComboBox<String> cbPatientId, cbRefClin, cbToClin, cbRefFacility, cbToFacility, cbUrgency, cbAppointmentId, cbStatus;

    private JButton btnAdd, btnUpdate, btnDelete;
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public ReferralView() {
        setLayout(new BorderLayout(10, 10));
        model = new DefaultTableModel(new Object[]{
            "ID", "Patient", "Ref Clin", "To Clin", "Ref Facility", "To Facility",
            "Date", "Urgency", "Reason", "Clinical Summary", "Requested Service",
            "Status", "Appointment", "Notes", "Created", "Updated"
        }, 0);

        table = new JTable(model);
        table.setRowHeight(18);
        // Load selected referral into form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedReferralIntoForm();
                btnUpdate.setEnabled(table.getSelectedRow() >= 0);
                btnDelete.setEnabled(table.getSelectedRow() >= 0);
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.EAST);
    // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.NORTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Referral Details"));
        panel.setPreferredSize(new Dimension(500, 700));

        txtId = createField();
        txtReason = createField();
        txtRequestedService = createField();
        txtCreatedDate = createField(); txtCreatedDate.setEditable(false);
        txtLastUpdated = createField(); txtLastUpdated.setEditable(false);

        cbPatientId = createCombo();
        cbRefClin = createCombo();
        cbToClin = createCombo();
        cbRefFacility = createCombo();
        cbToFacility = createCombo();
        cbAppointmentId = createCombo();
        cbUrgency = new JComboBox<>(new String[]{"Routine", "Urgent", "Non-urgent", "2-week wait"});
        cbStatus = new JComboBox<>(new String[]{"Pending", "Sent", "Received", "In Review", "Accepted", "Rejected", "Completed", "Cancelled"});

        txtReferralDate = createDateField();
        txtClinicalSummary = createArea();
        txtNotes = createArea();
        panel.add(new JLabel("Referral ID:"));
        panel.add(txtId);
        
        panel.add(new JLabel("Patient ID:"));
        panel.add(cbPatientId);
        
        panel.add(new JLabel("Referring Clinician:"));
        panel.add(cbRefClin);
        panel.add(new JLabel("Referred-To Clinician:"));
        panel.add(cbToClin);
        
        panel.add(new JLabel("Referring Facility:"));
        panel.add(cbRefFacility);
        
        panel.add(new JLabel("Referred-To Facility:"));
        panel.add(cbToFacility);
        
        panel.add(new JLabel("Referral Date:"));
        panel.add(txtReferralDate);
        
        panel.add(new JLabel("Urgency Level:"));
        panel.add(cbUrgency);
        
        panel.add(new JLabel("Referral Reason:"));
        panel.add(txtReason);
        
        panel.add(new JLabel("Requested Service:"));
        panel.add(txtRequestedService);
        
        panel.add(new JLabel("Status:"));
        panel.add(cbStatus);
        
        panel.add(new JLabel("Appointment ID:"));
        panel.add(cbAppointmentId);
        
        panel.add(new JLabel("Clinical Summary:"));
        panel.add(new JScrollPane(txtClinicalSummary));
        
        panel.add(new JLabel("Notes:"));
        panel.add(new JScrollPane(txtNotes));
        
        panel.add(new JLabel("Created Date:"));
        panel.add(txtCreatedDate);
        
        panel.add(new JLabel("Last Updated:"));
        panel.add(txtLastUpdated);
        return panel;
    }
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        btnAdd = new JButton("Create Referral");
        btnUpdate = new JButton("Update Selected");
        btnDelete = new JButton("Delete Selected");
        
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        
        return panel;
    }

    // Helper methods
    private JTextField createField() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(150, 25));
        return f;
    }

    private JTextArea createArea() {
        JTextArea a = new JTextArea(3, 15);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return a;
    }

    private JComboBox<String> createCombo() {
        return new JComboBox<>();
    }

    private JFormattedTextField createDateField() {
        DateFormatter df = new DateFormatter(dateFormat);
        JFormattedTextField f = new JFormattedTextField(df);
        f.setValue(java.util.Date.from(LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        return f;
    }
 //Load selected table row
    private void loadSelectedReferralIntoForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        
        txtId.setText(getTableValue(row, 0));
        setComboBoxValue(cbPatientId, getTableValue(row, 1));
        setComboBoxValue(cbRefClin, getTableValue(row, 2));
        setComboBoxValue(cbToClin, getTableValue(row, 3));
        setComboBoxValue(cbRefFacility, getTableValue(row, 4));
        setComboBoxValue(cbToFacility, getTableValue(row, 5));
        txtReferralDate.setText(getTableValue(row, 6));
        setComboBoxValue(cbUrgency, getTableValue(row, 7));
        txtReason.setText(getTableValue(row, 8));
        txtClinicalSummary.setText(getTableValue(row, 9));
        txtRequestedService.setText(getTableValue(row, 10));
        setComboBoxValue(cbStatus, getTableValue(row, 11));
        setComboBoxValue(cbAppointmentId, getTableValue(row, 12));
        txtNotes.setText(getTableValue(row, 13));
        txtCreatedDate.setText(getTableValue(row, 14));
        txtLastUpdated.setText(getTableValue(row, 15));
        
        txtId.setEditable(true);
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
        if (!value.isEmpty()) {
            comboBox.addItem(value);
            comboBox.setSelectedItem(value);
        }
    }
    public void setController(ReferralController controller) {
        this.controller = controller;
        if (controller != null) {
            //restrict access for patients
            if (controller.getCurrentUser().getRole().equals("patient")) {
                btnAdd.setEnabled(false);
                btnUpdate.setEnabled(false);
                btnDelete.setEnabled(false);
            }
            loadCombos();
            refreshAutoId();
            refreshDates();
        }
    }

    private void loadCombos() {
        cbPatientId.removeAllItems();
        cbRefClin.removeAllItems();
        cbToClin.removeAllItems();
        cbRefFacility.removeAllItems();
        cbToFacility.removeAllItems();
        cbAppointmentId.removeAllItems();

        for (String id : controller.getPatientIds()) cbPatientId.addItem(id);
        for (String id : controller.getClinicianIds()) {
            cbRefClin.addItem(id);
            cbToClin.addItem(id);
        }
        for (String id : controller.getFacilityIds()) {
            cbRefFacility.addItem(id);
            cbToFacility.addItem(id);
        }
        for (String id : controller.getAppointmentIds()) cbAppointmentId.addItem(id);
    }

    private void refreshAutoId() {
        txtId.setText(controller.getNextReferralId());
        txtId.setEditable(false);
    }

    private void refreshDates() {
        String today = LocalDate.now().format(localDateFormatter);
        txtCreatedDate.setText(today);
        txtLastUpdated.setText(today);
    }
//display the referrals 
    public void showReferrals(List<Referral> list) {
        model.setRowCount(0);
        for (Referral r : list) {
            model.addRow(new Object[]{
                r.getId(), r.getPatientId(), r.getReferringClinicianId(), r.getReferredToClinicianId(),
                r.getReferringFacilityId(), r.getReferredToFacilityId(), r.getReferralDate(),
                r.getUrgencyLevel(), r.getReferralReason(), r.getClinicalSummary(), r.getRequestedService(),
                r.getStatus(), r.getAppointmentId(), r.getNotes(), r.getCreatedDate(), r.getLastUpdated()
            });
        }
    }

    //create referrals 
    private void onAdd() {
        if (controller == null) return;

        String errors = validateForm();
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, errors, "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Referral r = new Referral(
            txtId.getText().trim(),
            (String) cbPatientId.getSelectedItem(),
            (String) cbRefClin.getSelectedItem(),
            (String) cbToClin.getSelectedItem(),
            (String) cbRefFacility.getSelectedItem(),
            (String) cbToFacility.getSelectedItem(),
            txtReferralDate.getText().trim(),
            (String) cbUrgency.getSelectedItem(),
            txtReason.getText().trim(),
            txtClinicalSummary.getText().trim(),
            txtRequestedService.getText().trim(),
            (String) cbStatus.getSelectedItem(),
            (String) cbAppointmentId.getSelectedItem(),
            txtNotes.getText().trim(),
            txtCreatedDate.getText().trim(),
            LocalDate.now().format(localDateFormatter)
        );

        controller.addReferral(r);
        JOptionPane.showMessageDialog(this, "Referral " + r.getId() + " created successfully.");
        refreshAutoId();
        refreshDates();
        clearForm();
    }

    // Update a referral 
    private void onUpdate() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a referral to update.");
            return;
        }

        String referralId = txtId.getText().trim();
        if (referralId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Referral ID is required.");
            return;
        }

        String errors = validateForm();
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, errors, "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Update referral " + referralId + "?", "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Referral updatedReferral = new Referral(
                referralId,
                (String) cbPatientId.getSelectedItem(),
                (String) cbRefClin.getSelectedItem(),
                (String) cbToClin.getSelectedItem(),
                (String) cbRefFacility.getSelectedItem(),
                (String) cbToFacility.getSelectedItem(),
                txtReferralDate.getText().trim(),
                (String) cbUrgency.getSelectedItem(),
                txtReason.getText().trim(),
                txtClinicalSummary.getText().trim(),
                txtRequestedService.getText().trim(),
                (String) cbStatus.getSelectedItem(),
                (String) cbAppointmentId.getSelectedItem(),
                txtNotes.getText().trim(),
                txtCreatedDate.getText().trim(),
                LocalDate.now().format(localDateFormatter)
            );

            controller.updateReferral(updatedReferral);
            JOptionPane.showMessageDialog(this, "Referral " + referralId + " updated successfully.");
            clearForm();
        }
    }
    // Delete a referral
    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a referral to delete.");
            return;
        }

        String referralId = model.getValueAt(row, 0).toString();
        String patientName = model.getValueAt(row, 1).toString();
        String reason = model.getValueAt(row, 8).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete referral " + referralId + "?\nPatient: " + patientName + "\nReason: " + reason,
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteReferral(referralId);
            JOptionPane.showMessageDialog(this, "Referral " + referralId + " deleted successfully.");
            clearForm();
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }
    private void clearForm() {
        refreshAutoId();
        refreshDates();
        txtReason.setText("");
        txtClinicalSummary.setText("");
        txtRequestedService.setText("");
        txtNotes.setText("");
        
        if (cbPatientId.getItemCount() > 0) cbPatientId.setSelectedIndex(0);
        if (cbRefClin.getItemCount() > 0) cbRefClin.setSelectedIndex(0);
        if (cbToClin.getItemCount() > 0) cbToClin.setSelectedIndex(0);
        if (cbRefFacility.getItemCount() > 0) cbRefFacility.setSelectedIndex(0);
        if (cbToFacility.getItemCount() > 0) cbToFacility.setSelectedIndex(0);
        if (cbUrgency.getItemCount() > 0) cbUrgency.setSelectedIndex(0);
        if (cbStatus.getItemCount() > 0) cbStatus.setSelectedIndex(0);
        if (cbAppointmentId.getItemCount() > 0) cbAppointmentId.setSelectedIndex(0);
    }

    private String validateForm() {
        StringBuilder sb = new StringBuilder();
        if (cbPatientId.getSelectedItem() == null) sb.append("- Patient ID required\n");
        if (cbRefClin.getSelectedItem() == null) sb.append("- Referring clinician required\n");
        if (cbToClin.getSelectedItem() == null) sb.append("- Referred-to clinician required\n");
        if (cbRefFacility.getSelectedItem() == null) sb.append("- Referring facility required\n");
        if (cbToFacility.getSelectedItem() == null) sb.append("- Referred-to facility required\n");
        if (txtReferralDate.getText().trim().isEmpty()) sb.append("- Referral date required\n");
        if (txtReason.getText().trim().isEmpty()) sb.append("- Referral reason required\n");
        if (txtClinicalSummary.getText().trim().isEmpty()) sb.append("- Clinical summary required\n");
        return sb.toString();
    }
}
package view;

import controller.*;
import model.User;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User currentUser;
    private StatusBarView statusBar;  // Changed to StatusBarView
    
    public MainFrame(PatientController pc,
                     ClinicianController cc,
                     AppointmentController ac,
                     PrescriptionController prc,
                     ReferralController rc,
                     User user) {
        
        super("Healthcare Management System - " + user.getName());
        this.currentUser = user;
        
        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create tabbed pane
        JTabbedPane tabs = setupTabs(pc, cc, ac, prc, rc);
        mainPanel.add(tabs, BorderLayout.CENTER);
        
        // Add status bar at bottom
        statusBar = new StatusBarView(user);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        
        // Test: Set a status message to confirm it's working
        statusBar.setStatus("System loaded successfully", "success");
    }
    
    private JTabbedPane setupTabs(PatientController pc, ClinicianController cc,
                                  AppointmentController ac, PrescriptionController prc,
                                  ReferralController rc) {
        JTabbedPane tabs = new JTabbedPane();
        String role = currentUser.getRole().toLowerCase();
        
        System.out.println("Setting up interface for: " + role);
        
        // ROLE-BASED TABS
        if (role.equals("patient")) {
            // Patient can: manage appointments, view prescriptions
            tabs.addTab("My Appointments", ac.getView());
            tabs.addTab("My Prescriptions", prc.getView());
            tabs.addTab("My Profile", pc.getView());
            
        } else if (role.equals("gp") || role.equals("specialist")) {
            // GP/Specialist can: manage referrals, create prescriptions
            tabs.addTab("My Patients", pc.getView());
            tabs.addTab("Appointments", ac.getView());
            tabs.addTab("Prescriptions", prc.getView());
            tabs.addTab("Referrals", rc.getView());
            
        } else if (role.equals("nurse")) {
            // Nurse can only view prescriptions
            JPanel nursePanel = new JPanel(new BorderLayout());
            nursePanel.add(new JLabel("Prescription Viewer - Nurse Access", SwingConstants.CENTER), BorderLayout.NORTH);
            nursePanel.add(prc.getView(), BorderLayout.CENTER);
            tabs.addTab("View Prescriptions", nursePanel);
            
        } else if (role.equals("staff")) {
            // Staff can: manage appointments, register patients
            tabs.addTab("Register Patients", pc.getView());
            tabs.addTab("Manage Appointments", ac.getView());
            
            // Staff can view but not edit prescriptions
            JPanel staffPrescriptions = new JPanel(new BorderLayout());
            staffPrescriptions.add(new JLabel("Prescription Viewer (Read-Only)", SwingConstants.CENTER), BorderLayout.NORTH);
            staffPrescriptions.add(prc.getView(), BorderLayout.CENTER);
            tabs.addTab("View Prescriptions", staffPrescriptions);
            
        } else if (role.equals("admin")) {
            // Admin can do everything
            tabs.addTab("Patients", pc.getView());
            tabs.addTab("Clinicians", cc.getView());
            tabs.addTab("Appointments", ac.getView());
            tabs.addTab("Prescriptions", prc.getView());
            tabs.addTab("Referrals", rc.getView());
            
        } else {
            // Default fallback (all tabs - for debugging)
            tabs.addTab("Patients", pc.getView());
            tabs.addTab("Clinicians", cc.getView());
            tabs.addTab("Appointments", ac.getView());
            tabs.addTab("Prescriptions", prc.getView());
            tabs.addTab("Referrals", rc.getView());
        }
        
        return tabs;
    }
    
    public StatusBarView getStatusBar() {
        return statusBar;
    }
}
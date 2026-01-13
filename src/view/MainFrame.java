package view;

import controller.*;
import model.User;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User currentUser;
    private StatusBarView statusBar;  //bttotm status bar to show user info
    
    //sets up the interface
    public MainFrame(PatientController pc,
                     ClinicianController cc,
                     AppointmentController ac,
                     PrescriptionController prc,
                     ReferralController rc,
                     User user) {
        
        super("Healthcare Management System - " + user.getName());
        this.currentUser = user;
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        //Different Tabs
        JTabbedPane tabs = setupTabs(pc, cc, ac, prc, rc);
        mainPanel.add(tabs, BorderLayout.CENTER);
        
        // Status bar at bottom shows user info and system messages
        statusBar = new StatusBarView(user);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);

        statusBar.setStatus("System loaded successfully", "success");
    }

    private JTabbedPane setupTabs(PatientController pc, ClinicianController cc,
                                  AppointmentController ac, PrescriptionController prc,
                                  ReferralController rc) {
        JTabbedPane tabs = new JTabbedPane();
        String role = currentUser.getRole().toLowerCase();
        
        System.out.println("Setting up interface for: " + role);
        
        // Patients only see their own data 
        if (role.equals("patient")) {
            tabs.addTab("My Appointments", ac.getView());
            tabs.addTab("My Prescriptions", prc.getView());
            tabs.addTab("My Profile", pc.getView());
            
        // Doctors get full clinical access
        } else if (role.equals("gp") || role.equals("specialist")) {
            tabs.addTab("My Patients", pc.getView());
            tabs.addTab("Appointments", ac.getView());
            tabs.addTab("Prescriptions", prc.getView());
            tabs.addTab("Referrals", rc.getView());
            
        // Nurses can only view prescriptions 
        } else if (role.equals("nurse")) {
            JPanel nursePanel = new JPanel(new BorderLayout());
            nursePanel.add(new JLabel("Prescription Viewer - Nurse Access", SwingConstants.CENTER), BorderLayout.NORTH);
            nursePanel.add(prc.getView(), BorderLayout.CENTER);
            tabs.addTab("View Prescriptions", nursePanel);
            
        // Staff handle patient registration and appointments
        } else if (role.equals("staff")) {
            tabs.addTab("Register Patients", pc.getView());
            tabs.addTab("Manage Appointments", ac.getView());
            
            JPanel staffPrescriptions = new JPanel(new BorderLayout());
            staffPrescriptions.add(new JLabel("Prescription Viewer (Read-Only)", SwingConstants.CENTER), BorderLayout.NORTH);
            staffPrescriptions.add(prc.getView(), BorderLayout.CENTER);
            tabs.addTab("View Prescriptions", staffPrescriptions);
            
        // Admins get complete system access
        } else if (role.equals("admin")) {
            tabs.addTab("Patients", pc.getView());
            tabs.addTab("Clinicians", cc.getView());
            tabs.addTab("Appointments", ac.getView());
            tabs.addTab("Prescriptions", prc.getView());
            tabs.addTab("Referrals", rc.getView());

        } else {
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
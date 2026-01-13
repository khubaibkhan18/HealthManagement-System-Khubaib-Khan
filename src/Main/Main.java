package Main;

import controller.*;
import model.*;
import view.*;
import javax.swing.*;

public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // the login screen
            showLoginScreen();
        });
    }
    
    private static void showLoginScreen() {
        LoginView loginView = new LoginView();
        
        JFrame loginFrame = new JFrame("Healthcare System - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setContentPane(loginView);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
        
        // the login button
        loginView.getLoginButton().addActionListener(e -> {
            String role = loginView.getSelectedRole();
            String id = loginView.getUserId();
            
            if (id.isEmpty()) {
                loginView.showError("Please enter your ID.");
                return;
            }
            
            // Loading repositories
            PatientRepository patientRepo = new PatientRepository("src/data/patients.csv");
            ClinicianRepository clinicianRepo = new ClinicianRepository("src/data/clinicians.csv");
            StaffRepository staffRepo = new StaffRepository("src/data/staff.csv");
            
            User user = null;
            
            // Authentication
            switch (role.toLowerCase()) {
                case "patient":
                    user = LoginManager.authenticatePatient(id, patientRepo);
                    break;
                    
                case "gp":
                case "specialist":
                case "nurse":
                    user = LoginManager.authenticateClinician(id, clinicianRepo);
                    break;
                    
                case "staff":
                    user = LoginManager.authenticateStaff(id, staffRepo);
                    break;
                    
                case "admin":
                    user = LoginManager.authenticateAdmin(id);
                    break;
            }
            
            if (user != null) {
                System.out.println("Login successful: " + user);
                loginFrame.dispose(); // close the login window
                openMainApplication(user); // Open the app
            } else {
                loginView.showError("Invalid ID: " + id + 
                    "\n\nTry these IDs:\n• Patient: P001\n• GP: C001\n• Specialist: C005\n• Nurse: C009\n• Staff: ST001\n• Admin: ADM001");
            }
        });
    }
    
    private static void openMainApplication(User user) {
        System.out.println("Opening application for: " + user.getRole() + " - " + user.getName());
        
        try {
            // REPOSITORIES
            PatientRepository pr = new PatientRepository("src/data/patients.csv");
            ClinicianRepository cr = new ClinicianRepository("src/data/clinicians.csv");
            FacilityRepository fr = new FacilityRepository("src/data/facilities.csv");
            AppointmentRepository ar = new AppointmentRepository("src/data/appointments.csv");
            PrescriptionRepository pResR = new PrescriptionRepository("src/data/prescriptions.csv");
            ReferralRepository rR = new ReferralRepository("src/data/referrals.csv");


            // REFERRAL MANAGER
            ReferralManager rm = ReferralManager.getInstance(
                    rR, pr, cr, fr,
                    "src/data/referrals_output.txt", user
            );

            // VIEWS
            PatientView pv = new PatientView();
            ClinicianView cv = new ClinicianView();
            AppointmentView av = new AppointmentView();
            PrescriptionView presV = new PrescriptionView();
            ReferralView rv = new ReferralView();

            // CONTROLLERS

            PatientController pc = new PatientController(
                    pr,   // PatientRepository
                    ar,   // AppointmentRepository 
                    pv,   // PatientView
                    user  // Current User
            );

            ClinicianController cc = new ClinicianController(cr, cv, user);

            AppointmentController ac = new AppointmentController(
                    ar,   // AppointmentRepository
                    pr,   // PatientRepository
                    cr,   // ClinicianRepository
                    fr,   // FacilityRepository
                    av,   // AppointmentView
                    user  // User
            );

            PrescriptionController prc = new PrescriptionController(
                    pResR,
                    pr,
                    cr,
                    ar,
                    presV,
                    user
            );

            ReferralController rc = new ReferralController(
                    rm,   // ReferralManager
                    pr,   // PatientRepository
                    cr,   // ClinicianRepository
                    fr,   // FacilityRepository
                    ar,   // AppointmentRepository
                    rv,   // ReferralView
                    user  // Current User
            );

            // MAIN FRAME
            MainFrame frame = new MainFrame(pc, cc, ac, prc, rc, user);
            frame.setVisible(true);
            
            // THE WELCOME MESSAGE
            JOptionPane.showMessageDialog(frame,
                "Welcome, " + user.getName() + "!\n" +
                "Role: " + user.getRole().toUpperCase() + "\n" +
                "ID: " + user.getId() + "\n\n" +
                "System loaded successfully.",
                "Welcome to Healthcare Management System",
                JOptionPane.INFORMATION_MESSAGE);

            // Updating the status bar
            frame.getStatusBar().setStatus("Welcome, " + user.getName() + "!", "success");

        } catch (Exception e) {
            System.err.println("Error opening application: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Failed to open application: " + e.getMessage(),
                "Application Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
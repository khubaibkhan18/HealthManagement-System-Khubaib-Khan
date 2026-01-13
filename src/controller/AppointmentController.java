package controller;

import model.*;
import view.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class AppointmentController {

    private final AppointmentRepository repo;
    private final PatientRepository patientRepo;
    private final ClinicianRepository clinicianRepo;
    private final FacilityRepository facilityRepo;
    private final AppointmentView view;
    private final User currentUser;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AppointmentController(AppointmentRepository repo,
                                 PatientRepository patientRepo,
                                 ClinicianRepository clinicianRepo,
                                 FacilityRepository facilityRepo,
                                 AppointmentView view,
                                 User user) {

        this.repo = repo;
        this.patientRepo = patientRepo;
        this.clinicianRepo = clinicianRepo;
        this.facilityRepo = facilityRepo;
        this.view = view;
        this.currentUser = user;

        view.setController(this);
        refreshAppointments();
        view.loadDropdowns(getPatientIds(), getClinicianIds(), getFacilityIds());
    }

    public AppointmentView getView() {
        return view;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void refreshAppointments() {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        List<Appointment> appointments;
        
        if (role.equals("patient")) {
            // Patient can only see THEIR OWN appointments
            appointments = repo.getAppointmentsByPatientId(userId);
        } else if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            // Clinicians see appointments assigned to them
            appointments = repo.getAppointmentsByClinicianId(userId);
        } else {
            // Staff, admin, etc. see all appointments
            appointments = repo.getAll();
        }
        
        view.showAppointments(appointments);
    }

    public String generateId() {
        return repo.generateNewId();
    }

    public List<String> getPatientIds() {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        if (role.equals("patient")) {
            // Patient can only select themselves
            List<String> ids = new ArrayList<>();
            ids.add(userId);
            return ids;
        } else {
            // Other roles see all patient IDs
            return patientRepo.getAllIds();
        }
    }

    public List<String> getClinicianIds() {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            // Clinicians can only select themselves for new appointments
            List<String> ids = new ArrayList<>();
            ids.add(userId);
            return ids;
        } else {
            // Patients and other roles see all clinician IDs
            return clinicianRepo.getAllIds();
        }
    }

    public List<String> getFacilityIds() {
        return facilityRepo.getAllIds();
    }

    public void addAppointment(Appointment a) {
        // Validate patient can only add appointments for themselves
        if (currentUser.getRole().equals("patient") && 
            !a.getPatientId().equals(currentUser.getId())) {
            JOptionPane.showMessageDialog(view, 
                "You can only create appointments for yourself.", 
                "Permission Denied", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        repo.addAndAppend(a);
        refreshAppointments();
    }

    public void deleteById(String id) {
        Appointment a = repo.findById(id);
        if (a != null) {
            // Validate patient can only delete their own appointments
            if (currentUser.getRole().equals("patient") && 
                !a.getPatientId().equals(currentUser.getId())) {
                JOptionPane.showMessageDialog(view, 
                    "You can only delete your own appointments.", 
                    "Permission Denied", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            repo.remove(a);
        }
        refreshAppointments();
    }
    
    // ============================================================
    // UPDATE APPOINTMENT
    // ============================================================
    public void updateAppointment(Appointment a) {
        // All roles except patient can update appointments
        if (currentUser.getRole().equals("patient")) {
            JOptionPane.showMessageDialog(view, 
                "You cannot modify appointments.", 
                "Permission Denied", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        repo.update(a);
        refreshAppointments();
    }
    
    // Helper method to check if current user can delete an appointment
    public boolean canDeleteAppointment(String appointmentId) {
        if (!currentUser.getRole().equals("patient")) {
            return true; // Non-patients can delete
        }
        
        Appointment a = repo.findById(appointmentId);
        return a != null && a.getPatientId().equals(currentUser.getId());
    }
}
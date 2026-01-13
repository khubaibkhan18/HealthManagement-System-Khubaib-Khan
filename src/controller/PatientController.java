package controller;

import model.Patient;
import model.PatientRepository;
import model.AppointmentRepository;
import model.Appointment;
import model.User;
import view.PatientView;
import java.util.ArrayList;
import java.util.List;

public class PatientController {

    private final PatientRepository repository;
    private final AppointmentRepository appointmentRepository;
    private final PatientView view;
    private final User currentUser;
    
    public PatientController(PatientRepository repository, 
                             AppointmentRepository appointmentRepository,
                             PatientView view, 
                             User user) {
        this.repository = repository;
        this.appointmentRepository = appointmentRepository;
        this.view = view;
        this.currentUser = user;
        this.view.setController(this);
        refreshView();
    }
    
    public PatientView getView() {
        return view;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }

    public void refreshView() {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        List<Patient> patients;
        
        if (role.equals("patient")) {
            // Patient can only see THEMSELVES
            patients = getPatientByIdAsList(userId);
        } else if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            // Clinicians can only see patients who have appointments with them
            patients = getPatientsByClinicianId(userId);
        } else {
            // Other roles (staff, admin) see all patients
            patients = repository.getAll();
        }
        
        view.showPatients(patients);
    }
    
    // Helper method to return a single patient as a list
    private List<Patient> getPatientByIdAsList(String patientId) {
        List<Patient> singlePatientList = new ArrayList<>();
        Patient patient = repository.findById(patientId);
        if (patient != null) {
            singlePatientList.add(patient);
        }
        return singlePatientList;
    }

    private List<Patient> getPatientsByClinicianId(String clinicianId) {
        List<Patient> clinicianPatients = new ArrayList<>();
        
        // Get all appointments for this clinician
        List<Appointment> clinicianAppointments = appointmentRepository.getAppointmentsByClinicianId(clinicianId);
        
        // Get unique patient IDs from these appointments
        List<String> patientIds = new ArrayList<>();
        for (Appointment a : clinicianAppointments) {
            String patientId = a.getPatientId();
            if (patientId != null && !patientId.isEmpty() && !patientIds.contains(patientId)) {
                patientIds.add(patientId);
            }
        }
        
        // Get patient objects for these IDs
        for (String patientId : patientIds) {
            Patient p = repository.findById(patientId);
            if (p != null) {
                clinicianPatients.add(p);
            }
        }
        
        return clinicianPatients;
    }

    public void addPatient(Patient p) {
        // Only staff/admin can add patients (not clinicians or patients)
        String role = currentUser.getRole();
        if (role.equals("patient") || role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            return;
        }
        
        repository.addAndAppend(p);
        refreshView();
    }

    public void deletePatient(Patient p) {
        // Only staff/admin can delete patients (not clinicians or patients)
        String role = currentUser.getRole();
        if (role.equals("patient") || role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            return;
        }
        
        repository.remove(p);
        refreshView();
    }

    public Patient findById(String id) {
        return repository.findById(id);
    }

    // ============================================================
    // UPDATE PATIENT WITH ROLE-BASED PERMISSIONS
    // ============================================================
    public void updatePatient(Patient updatedPatient) {
        String role = currentUser.getRole();
        if (role.equals("patient") || role.equals("gp") || 
            role.equals("specialist") || role.equals("nurse")) {
            return;
        }
        repository.update(updatedPatient);
        refreshView();
    }
}
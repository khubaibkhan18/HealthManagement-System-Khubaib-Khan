package controller;

import model.Patient;
import model.PatientRepository;
import model.User;
import view.PatientView;
import java.util.List;

public class PatientController {

    private final PatientRepository repository;
    private final PatientView view;
    private final User currentUser;
    
    public PatientController(PatientRepository repository, PatientView view, User user) {
        this.repository = repository;
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
        
        List<Patient> patients;
        
        if (role.equals("patient")) {
            // Patient can only see THEMSELVES
            String userId = currentUser.getId();
            patients = repository.getPatientsById(userId);
        } else if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            // Clinicians see ALL patients (for now - they need to see patients to create referrals/prescriptions)
            // In a real system, we'd filter by clinician's patients
            patients = repository.getAll();
        } else {
            // Other roles (staff, admin) see all patients
            patients = repository.getAll();
        }
        
        view.showPatients(patients);
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
}
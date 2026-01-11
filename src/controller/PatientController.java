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
        String userId = currentUser.getId();
        
        List<Patient> patients;
        
        if (role.equals("patient")) {
            // Patient can only see THEMSELVES
            patients = repository.getPatientsById(userId);
        } else {
            // Other roles see all patients
            patients = repository.getAll();
        }
        
        view.showPatients(patients);
    }

    public void addPatient(Patient p) {
        // Patients cannot add patients
        if (currentUser.getRole().equals("patient")) {
            return;
        }
        
        repository.addAndAppend(p);
        refreshView();
    }

    public void deletePatient(Patient p) {
        // Patients cannot delete patients
        if (currentUser.getRole().equals("patient")) {
            return;
        }
        
        repository.remove(p);
        refreshView();
    }

    public Patient findById(String id) {
        return repository.findById(id);
    }
}
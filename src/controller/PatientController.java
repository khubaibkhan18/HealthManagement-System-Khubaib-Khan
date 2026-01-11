package controller;

import model.Patient;
import model.PatientRepository;
import model.User;
import view.PatientView;
import java.util.List;

public class PatientController {

    private final PatientRepository repository;
    private final PatientView view;
    private final User currentUser;  // ADDED FIELD
    
    public PatientController(PatientRepository repository, PatientView view, User user) {
        this.repository = repository;
        this.view = view;
        this.currentUser = user;  // ADDED: Store user
        this.view.setController(this);
        refreshView();
    }
    
    public PatientView getView() {
        return view;
    }

    public void refreshView() {
        List<Patient> patients = repository.getAll();
        view.showPatients(patients);
    }

    public void addPatient(Patient p) {
        repository.addAndAppend(p);
        refreshView();
    }

    public void deletePatient(Patient p) {
        repository.remove(p);
        refreshView();
    }

    public Patient findById(String id) {
        return repository.findById(id);
    }
}
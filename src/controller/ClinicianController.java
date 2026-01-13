package controller;

import model.Clinician;
import model.ClinicianRepository;
import model.User;
import view.ClinicianView;
import java.util.List;

public class ClinicianController {

    public final ClinicianRepository repository;
    private final ClinicianView view;
    private final User currentUser;  
    public ClinicianController(ClinicianRepository repo, ClinicianView view, User user) {
        this.repository = repo;
        this.view = view;
        this.currentUser = user; 
        this.view.setController(this);
        refresh();
    }
 
    public User getCurrentUser() {
        return currentUser;
    }
    
    public ClinicianView getView() {
        return view;
    }
    
    // Generate ID
    public String generateId() {
        return repository.generateNewId();
    }


    public void refresh() {
        view.showClinicians(repository.getAll());
    }

    // ADD CLINICIAN
    public void addClinician(Clinician c) {
        repository.addAndAppend(c);
        refresh();
    }

    // DELETE CLINICIAN 
    public void deleteById(String id) {
        Clinician c = repository.findById(id);
        if (c != null) {
            repository.remove(c);
            refresh();
        }
    }
}
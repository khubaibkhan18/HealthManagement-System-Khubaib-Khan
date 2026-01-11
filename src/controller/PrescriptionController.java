package controller;

import model.Prescription;
import model.PrescriptionRepository;
import model.PatientRepository;
import model.ClinicianRepository;
import model.AppointmentRepository;
import model.Patient;
import model.Clinician;
import model.Appointment;
import model.User;
import view.PrescriptionView;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionController {

    private final PrescriptionRepository repository;
    private final PatientRepository patientRepository;
    private final ClinicianRepository clinicianRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionView view;
    private final User currentUser;

    public PrescriptionController(PrescriptionRepository repository,
                                  PatientRepository patientRepository,
                                  ClinicianRepository clinicianRepository,
                                  AppointmentRepository appointmentRepository,
                                  PrescriptionView view,
                                  User user) {

        this.repository = repository;
        this.patientRepository = patientRepository;
        this.clinicianRepository = clinicianRepository;
        this.appointmentRepository = appointmentRepository;
        this.view = view;
        this.currentUser = user;

        view.setController(this);

        // Populate dropdowns based on user role
        view.populateDropdowns(
                getPatientIds(),
                getClinicianIds(),
                repository.getMedicationOptions(),
                repository.getPharmacyOptions(),
                getAppointmentIds()
        );

        refreshView();
    }

    public PrescriptionView getView() {
        return view;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }

    public void refreshView() {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        List<Prescription> prescriptions;
        
        if (role.equals("patient")) {
            // Patient can only see THEIR OWN prescriptions
            prescriptions = repository.getPrescriptionsByPatientId(userId);
        } else {
            // Other roles see all prescriptions
            prescriptions = repository.getAll();
        }
        
        view.showPrescriptions(prescriptions);
        view.setNextId(repository.generateNewId());
    }

    // Expose lists for view
    public List<String> getPatientIds() {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        if (role.equals("patient")) {
            // Patient can only select themselves
            List<String> ids = new ArrayList<>();
            ids.add(userId);
            return ids;
        } else {
            List<String> ids = new ArrayList<>();
            for (Patient p : patientRepository.getAll()) {
                ids.add(p.getId());
            }
            return ids;
        }
    }

    public List<String> getClinicianIds() {
        List<String> ids = new ArrayList<>();
        for (Clinician c : clinicianRepository.getAll()) {
            ids.add(c.getId());
        }
        return ids;
    }

    public List<String> getAppointmentIds() {
        List<String> ids = new ArrayList<>();
        for (Appointment a : appointmentRepository.getAll()) {
            ids.add(a.getId());
        }
        return ids;
    }

    // ---------- CRUD called by view ----------
    public void addPrescription(Prescription p) {
        // Patients cannot add prescriptions
        if (currentUser.getRole().equals("patient")) {
            return;
        }
        
        repository.addAndAppend(p);
        refreshView();
    }

    public void updatePrescription(Prescription p) {
        // Patients cannot update prescriptions
        if (currentUser.getRole().equals("patient")) {
            return;
        }
        
        repository.update(p);
        refreshView();
    }

    public void deleteById(String id) {
        // Patients cannot delete prescriptions
        if (currentUser.getRole().equals("patient")) {
            return;
        }
        
        repository.removeById(id);
        refreshView();
    }
}
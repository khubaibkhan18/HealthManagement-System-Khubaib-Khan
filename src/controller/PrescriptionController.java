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
import model.PrescriptionManager;
public class PrescriptionController {

    private final PrescriptionRepository repository;
    private final PatientRepository patientRepository;
    private final ClinicianRepository clinicianRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionView view;
    private final User currentUser;
    private PrescriptionManager prescriptionManager;

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
        this.prescriptionManager = PrescriptionManager.getInstance();

        view.setController(this);

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
            
            prescriptions = repository.getPrescriptionsByPatientId(userId);
        } else if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            
            prescriptions = repository.getPrescriptionsByClinicianId(userId);
        } else {

            prescriptions = repository.getAll();
        }
        
        view.showPrescriptions(prescriptions);
        view.setNextId(repository.generateNewId());
    }

    // lists for view
    public List<String> getPatientIds() {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        if (role.equals("patient")) {
            // Patient can only select themselves
            List<String> ids = new ArrayList<>();
            ids.add(userId);
            return ids;
        } else {
            // Clinicians and others see all patients
            List<String> ids = new ArrayList<>();
            for (Patient p : patientRepository.getAll()) {
                ids.add(p.getId());
            }
            return ids;
        }
    }

    public List<String> getClinicianIds() {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            // Clinicians can only select themselves
            List<String> ids = new ArrayList<>();
            ids.add(userId);
            return ids;
        } else {
            // Others see all clinicians
            List<String> ids = new ArrayList<>();
            for (Clinician c : clinicianRepository.getAll()) {
                ids.add(c.getId());
            }
            return ids;
        }
    }

    public List<String> getAppointmentIds() {
        List<String> ids = new ArrayList<>();
        for (Appointment a : appointmentRepository.getAll()) {
            ids.add(a.getId());
        }
        return ids;
    }

    //CRUD 
    public void addPrescription(Prescription p) {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        // Patients can't add prescriptions
        if (role.equals("patient")) {
            return;
        }
        
        // Clinicians can add prescriptions
        if ((role.equals("gp") || role.equals("specialist") || role.equals("nurse")) &&
            !p.getClinicianId().equals(userId)) {
            return;
        }
        
        repository.addAndAppend(p);
        
        //prescription text file
        Patient patient = patientRepository.findById(p.getPatientId());
        Clinician clinician = clinicianRepository.findById(p.getClinicianId());
        
        // NULL CHECK
        if (patient != null && clinician != null) {
            prescriptionManager.writePrescriptionText(p, patient, clinician);
        } else {

            System.err.println("Could not find patient or clinician for prescription " + p.getId());
        }
        
        refreshView();
    }

    public void updatePrescription(Prescription p) {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        // Patients can't update prescriptions
        if (role.equals("patient")) {
            return;
        }
        
        // Clinicians can only update their own prescriptions
        if ((role.equals("gp") || role.equals("specialist") || role.equals("nurse")) &&
            !p.getClinicianId().equals(userId)) {
            return;
        }
        
        repository.update(p);
        refreshView();
    }

    public void deleteById(String id) {
        String role = currentUser.getRole();
        
        // Patients cannot delete prescriptions
        if (role.equals("patient")) {
            return;
        }
        
        if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            Prescription p = null;
            for (Prescription pres : repository.getAll()) {
                if (pres.getId().equals(id)) {
                    p = pres;
                    break;
                }
            }
            
            if (p != null && p.getClinicianId().equals(currentUser.getId())) {
                repository.removeById(id);
            }
        } else {
            // Staff and admin can delete any
            repository.removeById(id);
        }
        
        refreshView();
    }
}
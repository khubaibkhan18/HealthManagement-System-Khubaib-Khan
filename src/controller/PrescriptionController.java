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
        } else if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            // Clinicians see prescriptions they created
            prescriptions = repository.getPrescriptionsByClinicianId(userId);
        } else {
            // Staff, admin, etc. see all prescriptions
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

    // ---------- CRUD called by view ----------
    public void addPrescription(Prescription p) {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        // Patients cannot add prescriptions
        if (role.equals("patient")) {
            return;
        }
        
        // Clinicians can only add prescriptions for themselves
        if ((role.equals("gp") || role.equals("specialist") || role.equals("nurse")) &&
            !p.getClinicianId().equals(userId)) {
            return;
        }
        
        repository.addAndAppend(p);
        refreshView();
    }

    public void updatePrescription(Prescription p) {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        // Patients cannot update prescriptions
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
        
        // Clinicians can delete their own prescriptions
        if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            Prescription p = null;
            for (Prescription pres : repository.getAll()) {
                if (pres.getId().equals(id)) {
                    p = pres;
                    break;
                }
            }
            
            // Check if prescription belongs to this clinician
            if (p != null && p.getClinicianId().equals(currentUser.getId())) {
                repository.removeById(id);
            }
        } else {
            // Staff/admin can delete any
            repository.removeById(id);
        }
        
        refreshView();
    }
}
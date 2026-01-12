package controller;

import model.*;
import view.ReferralView;
import java.util.ArrayList;
import java.util.List;

public class ReferralController {

    private final ReferralManager referralManager;
    private final PatientRepository patientRepo;
    private final ClinicianRepository clinicianRepo;
    private final FacilityRepository facilityRepo;
    private final AppointmentRepository appointmentRepo;
    private final ReferralView view;
    private final User currentUser;

    public ReferralController(ReferralManager rm,
                              PatientRepository pr,
                              ClinicianRepository cr,
                              FacilityRepository fr,
                              AppointmentRepository ar,
                              ReferralView view,
                              User user) {

        this.referralManager = rm;
        this.patientRepo = pr;
        this.clinicianRepo = cr;
        this.facilityRepo = fr;
        this.appointmentRepo = ar;
        this.view = view;
        this.currentUser = user;

        this.view.setController(this);

        refreshReferrals();
    }

    public ReferralView getView() {
        return view;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }

    public void refreshReferrals() {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        List<Referral> allReferrals = referralManager.getAllReferrals();
        List<Referral> filteredReferrals = new ArrayList<>();
        
        if (role.equals("patient")) {
            // Patients see their own referrals
            for (Referral r : allReferrals) {
                if (r.getPatientId().equals(userId)) {
                    filteredReferrals.add(r);
                }
            }
        } else if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            // Clinicians see referrals they created (referring clinician)
            for (Referral r : allReferrals) {
                if (r.getReferringClinicianId().equals(userId)) {
                    filteredReferrals.add(r);
                }
            }
        } else {
            // Staff/admin see all referrals
            filteredReferrals = allReferrals;
        }
        
        view.showReferrals(filteredReferrals);
    }

    // ---------------------------------------------
    // COMBOBOX DATA
    // ---------------------------------------------
    public List<String> getPatientIds() {
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        List<String> ids = new ArrayList<>();
        
        if (role.equals("patient")) {
            // Patients can only select themselves
            ids.add(userId);
        } else if (role.equals("gp") || role.equals("specialist") || role.equals("nurse")) {
            // Clinicians see ALL patients (they need to see patients to create referrals)
            for (Patient p : patientRepo.getAll()) {
                ids.add(p.getId());
            }
        } else {
            // Staff/admin see all patients
            for (Patient p : patientRepo.getAll()) {
                ids.add(p.getId());
            }
        }
        return ids;
    }

    public List<String> getClinicianIds() {
        List<String> ids = new ArrayList<>();
        for (Clinician c : clinicianRepo.getAll()) {
            ids.add(c.getId());
        }
        return ids;
    }

    public List<String> getFacilityIds() {
        List<String> ids = new ArrayList<>();
        for (Facility f : facilityRepo.getAll()) {
            ids.add(f.getId());
        }
        return ids;
    }

    public List<String> getAppointmentIds() {
        List<String> ids = new ArrayList<>();
        for (Appointment a : appointmentRepo.getAll()) {
            ids.add(a.getId());
        }
        return ids;
    }

    // ---------------------------------------------
    // AUTO ID GENERATOR
    // ---------------------------------------------
    public String getNextReferralId() {
        int max = 0;
        for (Referral r : referralManager.getAllReferrals()) {
            String id = r.getId();
            if (id != null && id.startsWith("R")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        int next = max + 1;
        return String.format("R%03d", next);
    }

    // ---------------------------------------------
    // ADD REFERRAL
    // ---------------------------------------------
    public void addReferral(Referral r) {
        // Clinicians can only create referrals where they are the referring clinician
        String role = currentUser.getRole();
        String userId = currentUser.getId();
        
        if ((role.equals("gp") || role.equals("specialist") || role.equals("nurse")) &&
            !r.getReferringClinicianId().equals(userId)) {
            // Clinician trying to create referral as someone else - deny
            return;
        }
        
        referralManager.createReferral(r);
        refreshReferrals();
    }
}
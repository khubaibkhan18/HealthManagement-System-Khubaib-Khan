package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReferralManager {

    private final ReferralRepository referralRepository;
    private final PatientRepository patientRepository;
    private final ClinicianRepository clinicianRepository;
    private final FacilityRepository facilityRepository;
    private final String referralTextPath;
    private final User currentUser;

    // Package-private constructor - use factory method
    ReferralManager(ReferralRepository rr,
                   PatientRepository pr,
                   ClinicianRepository cr,
                   FacilityRepository fr,
                   String referralTextPath,
                   User user) {

        this.referralRepository = rr;
        this.patientRepository = pr;
        this.clinicianRepository = cr;
        this.facilityRepository = fr;
        this.referralTextPath = referralTextPath;
        this.currentUser = user;
    }

    public static ReferralManager createReferralManager(
            ReferralRepository rr,
            PatientRepository pr,
            ClinicianRepository cr,
            FacilityRepository fr,
            String referralTextPath,
            User user) {

        return new ReferralManager(rr, pr, cr, fr, referralTextPath, user);
    }

    public void createReferral(Referral r) {
        referralRepository.addAndAppend(r);
        writeReferralText(r);
    }

    public List<Referral> getAllReferrals() {
        return referralRepository.getAll();
    }

    public List<Referral> getReferralsByPatientId(String patientId) {
        List<Referral> filtered = new ArrayList<>();
        for (Referral r : referralRepository.getAll()) {
            if (r.getPatientId().equals(patientId)) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    public void deleteReferral(String referralId) {
        referralRepository.deleteById(referralId);
        rewriteTextFile();
    }

    public void updateReferral(Referral updatedReferral) {
        referralRepository.update(updatedReferral);
        rewriteTextFile();
    }

    public Referral findReferralById(String id) {
        return referralRepository.findById(id);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void writeReferralText(Referral r) {
        Patient patient = patientRepository.findById(r.getPatientId());
        Clinician referringClinician = clinicianRepository.findById(r.getReferringClinicianId());
        Clinician referredToClinician = clinicianRepository.findById(r.getReferredToClinicianId());
        Facility referringFacility = facilityRepository.findById(r.getReferringFacilityId());
        Facility referredToFacility = facilityRepository.findById(r.getReferredToFacilityId());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(referralTextPath, true))) {

            bw.write("==============================================");
            bw.newLine();
            bw.write("            REFERRAL SUMMARY REPORT           ");
            bw.newLine();
            bw.write("==============================================");
            bw.newLine();

            bw.write("Referral ID: " + r.getId());
            bw.newLine();

            if (patient != null) {
                bw.write("Patient: " + patient.getFullName() + " (" + r.getPatientId() + ")");
                bw.newLine();
            }

            if (referringClinician != null) {
                bw.write("Referring Clinician: " 
                    + referringClinician.getFullName()
                    + " (" + referringClinician.getTitle()
                    + " - " + referringClinician.getSpeciality() + ")");
                bw.newLine();
            }

            if (referredToClinician != null) {
                bw.write("Referred To: " 
                    + referredToClinician.getFullName()
                    + " (" + referredToClinician.getTitle()
                    + " - " + referredToClinician.getSpeciality() + ")");
                bw.newLine();
            }

            if (referringFacility != null) {
                bw.write("Referring Facility: " + referringFacility.getName() +
                         " (" + referringFacility.getType() + ")");
                bw.newLine();
            }

            if (referredToFacility != null) {
                bw.write("Referred To Facility: " + referredToFacility.getName() +
                         " (" + referredToFacility.getType() + ")");
                bw.newLine();
            }

            bw.write("Referral Date: " + r.getReferralDate());
            bw.newLine();

            bw.write("Urgency Level: " + r.getUrgencyLevel());
            bw.newLine();

            bw.write("Reason for Referral: " + r.getReferralReason());
            bw.newLine();

            bw.write("Requested Service: " + r.getRequestedService());
            bw.newLine();

            bw.write("Status: " + r.getStatus());
            bw.newLine();

            bw.write("Clinical Summary:");
            bw.newLine();
            bw.write(r.getClinicalSummary());
            bw.newLine();

            bw.write("Notes:");
            bw.newLine();
            bw.write(r.getNotes());
            bw.newLine();

            bw.write("Created Date: " + r.getCreatedDate());
            bw.newLine();

            bw.write("Last Updated: " + r.getLastUpdated());
            bw.newLine();

            bw.write("----------------------------------------------");
            bw.newLine();
            bw.newLine();

        } catch (IOException ex) {
            System.err.println("Failed to write referral text: " + ex.getMessage());
        }
    }

    private void rewriteTextFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(referralTextPath, false))) {
            // Write header
            bw.write("==============================================\n");
            bw.write("            REFERRAL SUMMARY REPORT           \n");
            bw.write("==============================================\n\n");

            for (Referral r : referralRepository.getAll()) {
                writeReferralToFile(bw, r);
            }
            
        } catch (IOException ex) {
            System.err.println("Failed to rewrite referral text file: " + ex.getMessage());
        }
    }
    
    private void writeReferralToFile(BufferedWriter bw, Referral r) throws IOException {
        Patient patient = patientRepository.findById(r.getPatientId());
        Clinician referringClinician = clinicianRepository.findById(r.getReferringClinicianId());
        Clinician referredToClinician = clinicianRepository.findById(r.getReferredToClinicianId());
        Facility referringFacility = facilityRepository.findById(r.getReferringFacilityId());
        Facility referredToFacility = facilityRepository.findById(r.getReferredToFacilityId());

        bw.write("Referral ID: " + r.getId() + "\n");

        if (patient != null) {
            bw.write("Patient: " + patient.getFullName() + " (" + r.getPatientId() + ")\n");
        }

        if (referringClinician != null) {
            bw.write("Referring Clinician: " 
                + referringClinician.getFullName()
                + " (" + referringClinician.getTitle()
                + " - " + referringClinician.getSpeciality() + ")\n");
        }

        if (referredToClinician != null) {
            bw.write("Referred To: " 
                + referredToClinician.getFullName()
                + " (" + referredToClinician.getTitle()
                + " - " + referredToClinician.getSpeciality() + ")\n");
        }

        if (referringFacility != null) {
            bw.write("Referring Facility: " + referringFacility.getName() +
                     " (" + referringFacility.getType() + ")\n");
        }

        if (referredToFacility != null) {
            bw.write("Referred To Facility: " + referredToFacility.getName() +
                     " (" + referredToFacility.getType() + ")\n");
        }

        bw.write("Referral Date: " + r.getReferralDate() + "\n");
        bw.write("Urgency Level: " + r.getUrgencyLevel() + "\n");
        bw.write("Reason for Referral: " + r.getReferralReason() + "\n");
        bw.write("Requested Service: " + r.getRequestedService() + "\n");
        bw.write("Status: " + r.getStatus() + "\n");
        bw.write("Clinical Summary:\n");
        bw.write(r.getClinicalSummary() + "\n");
        bw.write("Notes:\n");
        bw.write(r.getNotes() + "\n");
        bw.write("Created Date: " + r.getCreatedDate() + "\n");
        bw.write("Last Updated: " + r.getLastUpdated() + "\n");
        bw.write("----------------------------------------------\n\n");
    }
}
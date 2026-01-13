package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {

    private final List<Patient> patients = new ArrayList<>();
    private final String csvPath;

    public PatientRepository(String csvPath) {
        this.csvPath = csvPath;
        load();
    }

    public List<String> getAllIds() {
        List<String> ids = new ArrayList<>();
        for (Patient p : patients) ids.add(p.getId());
        return ids;
    }

    // LOAD PATIENTS FROM CSV 
    private void load() {
        try {
            for (String[] row : CsvUtils.readCsv(csvPath)) {

                Patient p = new Patient(
                        row[0],   // patient id
                        row[1],   // firstname
                        row[2],   // lastname
                        row[3],   // DOB
                        row[4],   // nhs number
                        row[5],   // gender
                        row[6],   // phone number
                        row[7],   // email
                        row[8],   // address
                        row[9],   // postcode
                        row[10],  // emergencycontact name
                        row[11],  // emergencycontact phone
                        row[12],  // registration date
                        row[13]   // gpsurgeryid
                );

                patients.add(p);
            }

        } catch (IOException ex) {
            System.err.println("Failed to load patients: " + ex.getMessage());
        }
    }
    public String generateNewId() {

        int max = 0;

        for (Patient p : patients) {
            try {
                int num = Integer.parseInt(p.getId().substring(1));
                if (num > max) max = num;
            } catch (Exception ignore) {}
        }

        return String.format("P%03d", max + 1);
    }

    // add and apend 
    public void addAndAppend(Patient p) {
        patients.add(p);

        try {
            CsvUtils.appendLine(csvPath, new String[]{
                    p.getId(),
                    p.getFirstName(),
                    p.getLastName(),
                    p.getDateOfBirth(),
                    p.getNhsNumber(),
                    p.getGender(),
                    p.getPhoneNumber(),
                    p.getEmail(),
                    p.getAddress(),
                    p.getPostcode(),
                    p.getEmergencyContactName(),
                    p.getEmergencyContactPhone(),
                    p.getRegistrationDate(),
                    p.getGpSurgeryId()
            });

        } catch (IOException ex) {
            System.err.println("Failed to append patient: " + ex.getMessage());
        }
    }

    public List<Patient> getAll() {
        return patients;
    }

    public void remove(Patient p) {
        patients.remove(p);
    }

    public Patient findById(String id) {
        for (Patient p : patients) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    // FILTERING PATIENTS

    public List<Patient> getPatientsById(String patientId) {
        List<Patient> filtered = new ArrayList<>();
        for (Patient p : patients) {
            if (p.getId().equals(patientId)) {
                filtered.add(p);
            }
        }
        return filtered;
    }
    // UPDATE AND SAVE PATIENT TO CSV
    public void update(Patient updatedPatient) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId().equals(updatedPatient.getId())) {
                patients.set(i, updatedPatient);
                writeAllToCSV();
                return;
            }
        }
    }

    private void writeAllToCSV() {
        List<String[]> rows = new ArrayList<>();
        for (Patient p : patients) {
            rows.add(new String[]{
                    p.getId(), p.getFirstName(), p.getLastName(),
                    p.getDateOfBirth(), p.getNhsNumber(), p.getGender(),
                    p.getPhoneNumber(), p.getEmail(), p.getAddress(),
                    p.getPostcode(), p.getEmergencyContactName(),
                    p.getEmergencyContactPhone(), p.getRegistrationDate(),
                    p.getGpSurgeryId()
            });
        }
        try {
            CsvUtils.writeCsv(csvPath, rows);
        } catch (IOException ex) {
            System.err.println("Failed to write patients CSV: " + ex.getMessage());
        }
    }
}
package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrescriptionManager {
    private static PrescriptionManager instance;
    private String outputPath = "src/data/prescriptions_output.txt";
    
    private PrescriptionManager() {}
    
    public static synchronized PrescriptionManager getInstance() {
        if (instance == null) {
            instance = new PrescriptionManager();
        }
        return instance;
    }
    
    public void writePrescriptionText(Prescription p, Patient patient, Clinician clinician) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath, true))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
            bw.write("==============================================");
            bw.newLine();
            bw.write("            PRESCRIPTION REPORT               ");
            bw.newLine();
            bw.write("==============================================");
            bw.newLine();
            bw.write("Prescription ID: " + p.getId());
            bw.newLine();
            bw.write("Patient: " + patient.getFullName() + " (" + p.getPatientId() + ")");
            bw.newLine();
            bw.write("Clinician: " + clinician.getFullName() + " (" + p.getClinicianId() + ")");
            bw.newLine();
            bw.write("Date: " + p.getPrescriptionDate());
            bw.newLine();
            bw.write("Medication: " + p.getMedication());
            bw.newLine();
            bw.write("Dosage: " + p.getDosage() + " | Frequency: " + p.getFrequency());
            bw.newLine();
            bw.write("Duration: " + p.getDurationDays() + " days | Quantity: " + p.getQuantity());
            bw.newLine();
            bw.write("Pharmacy: " + p.getPharmacyName());
            bw.newLine();
            bw.write("Status: " + p.getStatus());
            bw.newLine();
            bw.write("Instructions: " + p.getInstructions());
            bw.newLine();
            bw.write("Generated: " + sdf.format(new Date()));
            bw.newLine();
            bw.write("----------------------------------------------");
            bw.newLine();
            bw.newLine();
            
        } catch (IOException ex) {
            System.err.println("Failed to write prescription text: " + ex.getMessage());
        }
    }
}
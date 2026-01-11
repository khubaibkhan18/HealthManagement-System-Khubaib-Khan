package model;

public class LoginManager {
    
    public static User authenticatePatient(String patientId, PatientRepository patientRepo) {
        System.out.println("Checking patient: " + patientId);
        Patient patient = patientRepo.findById(patientId);
        if (patient != null) {
            System.out.println("Found patient: " + patient.getFullName());
            return new User("patient", patientId, patient.getFullName());
        }
        System.out.println("Patient not found");
        return null;
    }
    
    public static User authenticateClinician(String clinicianId, ClinicianRepository clinicianRepo) {
        System.out.println("Checking clinician: " + clinicianId);
        Clinician clinician = clinicianRepo.findById(clinicianId);
        if (clinician != null) {
            System.out.println("Found clinician: " + clinician.getFullName());
            System.out.println("Title: " + clinician.getTitle());
            System.out.println("Speciality: " + clinician.getSpeciality());
            
            String role = determineClinicianRole(clinician.getTitle(), clinician.getSpeciality());
            System.out.println("Detected role: " + role);
            
            return new User(role, clinicianId, clinician.getFullName());
        }
        System.out.println("Clinician not found");
        return null;
    }
    
    public static User authenticateStaff(String staffId, StaffRepository staffRepo) {
        System.out.println("Checking staff: " + staffId);
        for (Staff staff : staffRepo.getAll()) {
            System.out.println("Staff ID in file: " + staff.getId());
            if (staff.getId().equals(staffId)) {
                System.out.println("Found staff: " + staff.getName());
                return new User("staff", staffId, staff.getName());
            }
        }
        System.out.println("Staff not found");
        return null;
    }
    
    public static User authenticateAdmin(String adminId) {
        System.out.println("Checking admin: " + adminId);
        if (adminId.equals("ADM001")) {
            System.out.println("Admin authenticated");
            return new User("admin", "ADM001", "System Administrator");
        }
        System.out.println("Admin not found");
        return null;
    }
    
    private static String determineClinicianRole(String title, String speciality) {
        if (title == null) return "gp";
        
        String lowerTitle = title.toLowerCase();
        
        // Simple detection
        if (lowerTitle.contains("nurse")) {
            return "nurse";
        }
        if (lowerTitle.contains("consultant")) {
            return "specialist";
        }
        if (lowerTitle.contains("dr.") || lowerTitle.contains("doctor")) {
            return "gp";
        }
        
        return "gp"; // default
    }
}
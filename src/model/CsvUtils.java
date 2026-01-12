package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {

    public static List<String[]> readCsv(String path) throws IOException {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String line;
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null) {

                if (!headerSkipped) { headerSkipped = true; continue; }

                // Split on commas that are NOT inside quotes
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                // Remove surrounding quotes (optional)
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].replaceAll("^\"|\"$", "").trim();
                }

                rows.add(values);
            }
        }
        return rows;
    }

    public static void appendLine(String path, String[] values) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(String.join(",", values));
            bw.newLine();
        }
    }
    
    // ADD THIS NEW METHOD
    public static void writeCsv(String path, List<String[]> rows) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, false))) {
            // Write header first (you might want to customize this)
            String header = "referral_id,patient_id,referring_clinician_id,referred_to_clinician_id," +
                           "referring_facility_id,referred_to_facility_id,referral_date,urgency_level," +
                           "referral_reason,clinical_summary,requested_service,status,appointment_id," +
                           "notes,created_date,last_updated";
            bw.write(header);
            bw.newLine();
            
            // Write all rows
            for (String[] row : rows) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        }
    }
}

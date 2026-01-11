package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final List<User> users = new ArrayList<>();
    private final String csvPath;
    
    public UserRepository(String csvPath) {
        this.csvPath = csvPath;
        loadUsers();
    }
    
    private void loadUsers() {
        try {
            List<String[]> rows = CsvUtils.readCsv(csvPath);
            System.out.println("Loading users from: " + csvPath);
            System.out.println("Found " + rows.size() + " rows");
            
            for (int i = 0; i < rows.size(); i++) {
                String[] row = rows.get(i);
                
                // Skip header row
                if (i == 0 && row[0].equalsIgnoreCase("role")) {
                    System.out.println("Skipping header row");
                    continue;
                }
                
                // Check if we have enough columns (5 columns: role,id,password,first_name,last_name)
                if (row.length >= 5) {
                    User user = new User(
                        row[0], // role
                        row[1], // id  
                        row[2], // password
                        row[3], // first_name
                        row[4]  // last_name
                    );
                    users.add(user);
                    System.out.println("Loaded user: " + user);
                } else if (row.length >= 4) {
                    // For old format (4 columns: role,id,password,name)
                    User user = new User(
                        row[0], // role
                        row[1], // id  
                        row[2], // password
                        row[3]  // name
                    );
                    users.add(user);
                    System.out.println("Loaded user (old format): " + user);
                } else {
                    System.err.println("ERROR: Invalid user row (expected 4-5 columns, got " + row.length + "): " + String.join(",", row));
                }
            }
            
            System.out.println("Successfully loaded " + users.size() + " users");
            
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load users: " + e.getMessage());
            // Create default admin if file doesn't exist
            createDefaultUsers();
        }
    }
    
    private void createDefaultUsers() {
        // Default admin user with separate first/last name
        users.add(new User("admin", "ADM001", "admin123", "System", "Administrator"));
        System.out.println("Created default admin user (ADM001/admin123)");
    }
    
    public User authenticate(String role, String id, String password) {
        System.out.println("Authenticating: role=" + role + ", id=" + id);
        
        for (User user : users) {
            boolean roleMatches = user.getRole().equalsIgnoreCase(role.trim());
            boolean idMatches = user.getId().equalsIgnoreCase(id.trim());
            
            if (roleMatches && idMatches) {
                // Optional: Add password check here
                // if (user.getPassword().equals(password)) { ... }
                System.out.println("Authentication SUCCESS for: " + user);
                return user;
            }
        }
        
        System.out.println("Authentication FAILED for: " + role + "/" + id);
        return null;
    }
    
    // For patient self-registration
    public void addUser(User user) {
        users.add(user);
        try {
            CsvUtils.appendLine(csvPath, new String[]{
                user.getRole(),
                user.getId(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName()
            });
            System.out.println("Added new user: " + user);
        } catch (IOException e) {
            System.err.println("Failed to save user: " + e.getMessage());
        }
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    // Check if ID already exists
    public boolean userIdExists(String id) {
        for (User user : users) {
            if (user.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
    
    // Find user by ID
    public User findById(String id) {
        for (User user : users) {
            if (user.getId().equalsIgnoreCase(id)) {
                return user;
            }
        }
        return null;
    }
    
    // Generate next patient ID
    public String generateNextPatientId() {
        int max = 0;
        for (User user : users) {
            if (user.getRole().equals("patient") && user.getId().startsWith("P")) {
                try {
                    int num = Integer.parseInt(user.getId().substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
        return String.format("P%03d", max + 1);
    }
}
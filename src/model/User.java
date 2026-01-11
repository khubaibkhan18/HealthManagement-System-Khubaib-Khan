package model;

public class User {
    private String role;  // "patient", "gp", "specialist", "nurse", "staff", "admin"
    private String id;    // "P001", "C001", "ST001", "ADM001"
    private String name;  // Full name
    
    public User(String role, String id, String name) {
        this.role = role;
        this.id = id;
        this.name = name;
    }
    
    // Getters
    public String getRole() { return role; }
    public String getId() { return id; }
    public String getName() { return name; }
    
    @Override
    public String toString() {
        return name + " (" + role.toUpperCase() + " - " + id + ")";
    }
}
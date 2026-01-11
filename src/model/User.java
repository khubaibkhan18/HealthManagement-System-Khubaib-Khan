package model;

public class User {
    private String role;
    private String id;
    private String password;
    private String firstName;
    private String lastName;
    
    // Constructor with first/last name
    public User(String role, String id, String password, String firstName, String lastName) {
        this.role = role;
        this.id = id;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Old constructor for backward compatibility (with single name field)
    public User(String role, String id, String password, String name) {
        this.role = role;
        this.id = id;
        this.password = password;
        // Split name into first and last if possible
        String[] nameParts = name.split(" ");
        this.firstName = nameParts.length > 0 ? nameParts[0] : name;
        this.lastName = nameParts.length > 1 ? nameParts[1] : "";
    }
    
    // Getters
    public String getRole() { return role; }
    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    
    // For backward compatibility - returns full name
    public String getName() { 
        return firstName + (lastName != null && !lastName.isEmpty() ? " " + lastName : "");
    }
    
    @Override
    public String toString() {
        return String.format("User[role=%s, id=%s, name=%s %s]", 
            role, id, firstName, lastName);
    }
}
package model;

public class User {
    private String role;
    private String id;
    private String name;
    
    public User(String role, String id, String name) {
        this.role = role;
        this.id = id;
        this.name = name;
    }
    
    //getters 
    public String getRole() { return role; }
    public String getId() { return id; }
    public String getName() { return name; }
    
    @Override
    public String toString() {
        return name + " (" + role.toUpperCase() + " - " + id + ")";
    }
}
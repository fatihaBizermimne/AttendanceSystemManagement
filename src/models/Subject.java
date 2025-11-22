package models;

public class Subject {
    private int suid;
    private String name;
    
    public Subject(int suid, String name) {
        this.suid = suid;
        this.name = name;
    }
    
    // Getters and setters
    public int getSuid() { return suid; }
    public void setSuid(int suid) { this.suid = suid; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    @Override
    public String toString() {
        return name;
    }
}
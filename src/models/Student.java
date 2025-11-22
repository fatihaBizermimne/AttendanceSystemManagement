package models;

public class Student {
    private String pid;
    private String name;
    private String password;
    
    public Student(String pid, String name, String password) {
        this.pid = pid;
        this.name = name;
        this.password = password;
    }
    
    // Getters and setters
    public String getPid() { return pid; }
    public void setPid(String pid) { this.pid = pid; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    @Override
    public String toString() {
        return name + " (" + pid + ")";
    }
}
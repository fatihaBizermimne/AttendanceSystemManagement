package models;

import java.sql.Timestamp;

public class Attendance {
    private int aid;
    private Timestamp stime;
    private Timestamp etime;
    private String pid;
    private int suid;
    private int status;
    private String studentName; // Additional field for student name
    private String subjectName; // Additional field for subject name
    
    // Constructor for basic attendance
    @SuppressWarnings("exports")
	public Attendance(int aid, Timestamp stime, Timestamp etime, String pid, int suid, int status) {
        this.aid = aid;
        this.stime = stime;
        this.etime = etime;
        this.pid = pid;
        this.suid = suid;
        this.status = status;
    }
    
    // Constructor with all fields including names
    @SuppressWarnings("exports")
	public Attendance(int aid, Timestamp stime, Timestamp etime, String pid, int suid, int status, 
                     String studentName, String subjectName) {
        this.aid = aid;
        this.stime = stime;
        this.etime = etime;
        this.pid = pid;
        this.suid = suid;
        this.status = status;
        this.studentName = studentName;
        this.subjectName = subjectName;
    }
    
    // Getters and setters
    public int getAid() { return aid; }
    public void setAid(int aid) { this.aid = aid; }
    
    @SuppressWarnings("exports")
	public Timestamp getStime() { return stime; }
    @SuppressWarnings("exports")
	public void setStime(Timestamp stime) { this.stime = stime; }
    
    @SuppressWarnings("exports")
	public Timestamp getEtime() { return etime; }
    public void setEtime(@SuppressWarnings("exports") Timestamp etime) { this.etime = etime; }
    
    public String getPid() { return pid; }
    public void setPid(String pid) { this.pid = pid; }
    
    public int getSuid() { return suid; }
    public void setSuid(int suid) { this.suid = suid; }
    
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    
    // Utility methods
    public String getStatusText() {
        return status == 1 ? "Present" : "Absent";
    }
    
    public String getStatusWithIcon() {
        return status == 1 ? "✅ Present" : "❌ Absent";
    }
    
    @Override
    public String toString() {
        return "Attendance{" +
                "aid=" + aid +
                ", pid='" + pid + '\'' +
                ", suid=" + suid +
                ", status=" + getStatusText() +
                ", stime=" + stime +
                '}';
    }
}
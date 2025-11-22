package dao;

import models.Attendance;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    
    // Mark attendance for a student
    public static boolean markAttendance(String studentPid, int subjectId, int status) throws SQLException {
        String sql = "INSERT INTO attendance (pid, suid, status) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentPid);
            stmt.setInt(2, subjectId);
            stmt.setInt(3, status);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Mark attendance for multiple students at once
    public static boolean markMultipleAttendance(List<String> studentPids, int subjectId, int status) throws SQLException {
        String sql = "INSERT INTO attendance (pid, suid, status) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (String studentPid : studentPids) {
                stmt.setString(1, studentPid);
                stmt.setInt(2, subjectId);
                stmt.setInt(3, status);
                stmt.addBatch();
            }
            
            int[] results = stmt.executeBatch();
            return results.length > 0;
        }
    }
    
    // Get attendance records for a specific student with proper null handling
    public static List<Attendance> getAttendanceByStudent(String studentPid) throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, s.name as subject_name, st.name as student_name " +
                    "FROM attendance a " +
                    "LEFT JOIN subjects s ON a.suid = s.suid " +
                    "LEFT JOIN students st ON a.pid = st.pid " +
                    "WHERE a.pid = ? ORDER BY a.stime DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentPid);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Attendance attendance = new Attendance(
                    rs.getInt("aid"),
                    rs.getTimestamp("stime"),
                    rs.getTimestamp("etime"),
                    rs.getString("pid"),
                    rs.getInt("suid"),
                    rs.getInt("status")
                );
                // Handle null values for names
                String studentName = rs.getString("student_name");
                String subjectName = rs.getString("subject_name");
                
                attendance.setStudentName(studentName != null ? studentName : "Student " + rs.getString("pid"));
                attendance.setSubjectName(subjectName != null ? subjectName : "Subject " + rs.getInt("suid"));
                
                attendanceList.add(attendance);
            }
        }
        return attendanceList;
    }
    
    // Get all attendance records with proper student and subject names and null handling
    public static List<Attendance> getAllAttendance() throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, s.name as subject_name, st.name as student_name " +
                    "FROM attendance a " +
                    "LEFT JOIN subjects s ON a.suid = s.suid " +
                    "LEFT JOIN students st ON a.pid = st.pid " +
                    "ORDER BY a.stime DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Attendance attendance = new Attendance(
                    rs.getInt("aid"),
                    rs.getTimestamp("stime"),
                    rs.getTimestamp("etime"),
                    rs.getString("pid"),
                    rs.getInt("suid"),
                    rs.getInt("status")
                );
                // Handle null values for names
                String studentName = rs.getString("student_name");
                String subjectName = rs.getString("subject_name");
                
                attendance.setStudentName(studentName != null ? studentName : "Student " + rs.getString("pid"));
                attendance.setSubjectName(subjectName != null ? subjectName : "Subject " + rs.getInt("suid"));
                
                attendanceList.add(attendance);
            }
        }
        return attendanceList;
    }
    public static List<Object[]> getAttendanceHistory(String studentPid, int subjectId) throws SQLException {
        List<Object[]> history = new ArrayList<>();
        String sql = "SELECT stime, status FROM attendance WHERE pid = ? AND suid = ? ORDER BY stime DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentPid);
            stmt.setInt(2, subjectId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] record = new Object[2];
                record[0] = rs.getTimestamp("stime"); // Use getTimestamp for stime
                record[1] = rs.getInt("status");
                history.add(record);
            }
        }
        return history;
    }
    // Get attendance records for a specific subject
    public static List<Attendance> getAttendanceBySubject(int subjectId) throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, s.name as subject_name, st.name as student_name " +
                    "FROM attendance a " +
                    "LEFT JOIN subjects s ON a.suid = s.suid " +
                    "LEFT JOIN students st ON a.pid = st.pid " +
                    "WHERE a.suid = ? ORDER BY a.stime DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Attendance attendance = new Attendance(
                    rs.getInt("aid"),
                    rs.getTimestamp("stime"),
                    rs.getTimestamp("etime"),
                    rs.getString("pid"),
                    rs.getInt("suid"),
                    rs.getInt("status")
                );
                // Handle null values for names
                String studentName = rs.getString("student_name");
                String subjectName = rs.getString("subject_name");
                
                attendance.setStudentName(studentName != null ? studentName : "Student " + rs.getString("pid"));
                attendance.setSubjectName(subjectName != null ? subjectName : "Subject " + rs.getInt("suid"));
                
                attendanceList.add(attendance);
            }
        }
        return attendanceList;
    }
    
    // Get attendance records by date range
    @SuppressWarnings("exports")
	public static List<Attendance> getAttendanceByDateRange(Date startDate, Date endDate) throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, s.name as subject_name, st.name as student_name " +
                    "FROM attendance a " +
                    "LEFT JOIN subjects s ON a.suid = s.suid " +
                    "LEFT JOIN students st ON a.pid = st.pid " +
                    "WHERE DATE(a.stime) BETWEEN ? AND ? " +
                    "ORDER BY a.stime DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Attendance attendance = new Attendance(
                    rs.getInt("aid"),
                    rs.getTimestamp("stime"),
                    rs.getTimestamp("etime"),
                    rs.getString("pid"),
                    rs.getInt("suid"),
                    rs.getInt("status")
                );
                // Handle null values for names
                String studentName = rs.getString("student_name");
                String subjectName = rs.getString("subject_name");
                
                attendance.setStudentName(studentName != null ? studentName : "Student " + rs.getString("pid"));
                attendance.setSubjectName(subjectName != null ? subjectName : "Subject " + rs.getInt("suid"));
                
                attendanceList.add(attendance);
            }
        }
        return attendanceList;
    }
    
    // Update attendance status
    public static boolean updateAttendance(int aid, int status) throws SQLException {
        String sql = "UPDATE attendance SET status = ? WHERE aid = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, status);
            stmt.setInt(2, aid);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Delete attendance record
    public static boolean deleteAttendance(int aid) throws SQLException {
        String sql = "DELETE FROM attendance WHERE aid = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, aid);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Get attendance by ID
    public static Attendance getAttendanceById(int aid) throws SQLException {
        String sql = "SELECT a.*, s.name as subject_name, st.name as student_name " +
                    "FROM attendance a " +
                    "LEFT JOIN subjects s ON a.suid = s.suid " +
                    "LEFT JOIN students st ON a.pid = st.pid " +
                    "WHERE a.aid = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, aid);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Attendance attendance = new Attendance(
                    rs.getInt("aid"),
                    rs.getTimestamp("stime"),
                    rs.getTimestamp("etime"),
                    rs.getString("pid"),
                    rs.getInt("suid"),
                    rs.getInt("status")
                );
                // Handle null values for names
                String studentName = rs.getString("student_name");
                String subjectName = rs.getString("subject_name");
                
                attendance.setStudentName(studentName != null ? studentName : "Student " + rs.getString("pid"));
                attendance.setSubjectName(subjectName != null ? subjectName : "Subject " + rs.getInt("suid"));
                
                return attendance;
            }
        }
        return null;
    }
    
    // Check if attendance already exists for student and subject on the same day
    @SuppressWarnings("exports")
	public static boolean attendanceExists(String studentPid, int subjectId, Date date) throws SQLException {
        String sql = "SELECT COUNT(*) FROM attendance WHERE pid = ? AND suid = ? AND DATE(stime) = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentPid);
            stmt.setInt(2, subjectId);
            stmt.setDate(3, date);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    // Get attendance statistics for a student
    public static int[] getStudentAttendanceStats(String studentPid) throws SQLException {
        int[] stats = new int[2]; // [present, total]
        String sql = "SELECT " +
                    "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as present_count, " +
                    "COUNT(*) as total_count " +
                    "FROM attendance WHERE pid = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentPid);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                stats[0] = rs.getInt("present_count");
                stats[1] = rs.getInt("total_count");
            }
        }
        return stats;
    }
    
    // Get attendance statistics for a subject
    public static int[] getSubjectAttendanceStats(int subjectId) throws SQLException {
        int[] stats = new int[2]; // [present, total]
        String sql = "SELECT " +
                    "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as present_count, " +
                    "COUNT(*) as total_count " +
                    "FROM attendance WHERE suid = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                stats[0] = rs.getInt("present_count");
                stats[1] = rs.getInt("total_count");
            }
        }
        return stats;
    }
    
    // Get today's attendance records
    public static List<Attendance> getTodaysAttendance() throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, s.name as subject_name, st.name as student_name " +
                    "FROM attendance a " +
                    "LEFT JOIN subjects s ON a.suid = s.suid " +
                    "LEFT JOIN students st ON a.pid = st.pid " +
                    "WHERE DATE(a.stime) = CURDATE() " +
                    "ORDER BY a.stime DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Attendance attendance = new Attendance(
                    rs.getInt("aid"),
                    rs.getTimestamp("stime"),
                    rs.getTimestamp("etime"),
                    rs.getString("pid"),
                    rs.getInt("suid"),
                    rs.getInt("status")
                );
                // Handle null values for names
                String studentName = rs.getString("student_name");
                String subjectName = rs.getString("subject_name");
                
                attendance.setStudentName(studentName != null ? studentName : "Student " + rs.getString("pid"));
                attendance.setSubjectName(subjectName != null ? subjectName : "Subject " + rs.getInt("suid"));
                
                attendanceList.add(attendance);
            }
        }
        return attendanceList;
    }
    
    // Get attendance count by status for dashboard
    public static int getAttendanceCountByStatus(int status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM attendance WHERE status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, status);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    // Get total attendance count
    public static int getTotalAttendanceCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM attendance";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
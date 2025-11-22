package dao;

import models.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    
    public static List<Subject> getAllSubjects() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                subjects.add(new Subject(
                    rs.getInt("suid"),
                    rs.getString("name")
                ));
            }
        }
        return subjects;
    }
    
    public static boolean addSubject(String name) throws SQLException {
        String sql = "INSERT INTO subjects (name) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            return stmt.executeUpdate() > 0;
        }
    }
    
 // In SubjectDAO class
    public static boolean deleteSubject(int suid) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // First delete all attendance records for this subject
            String deleteAttendanceSQL = "DELETE FROM attendance WHERE suid = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteAttendanceSQL)) {
                stmt.setInt(1, suid);
                stmt.executeUpdate();
            }
            
            // Then delete the subject
            String deleteSubjectSQL = "DELETE FROM subjects WHERE suid = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteSubjectSQL)) {
                stmt.setInt(1, suid);
                int rowsAffected = stmt.executeUpdate();
                
                conn.commit(); // Commit transaction
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback on error
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
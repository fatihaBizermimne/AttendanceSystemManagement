package dao;

import models.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    
    public static Student getStudentByPid(String pid) throws SQLException {
        String sql = "SELECT * FROM students WHERE pid = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, pid);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Student(
                    rs.getString("pid"),
                    rs.getString("name"),
                    rs.getString("password")
                );
            }
        }
        return null;
    }
    
    public static List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(new Student(
                    rs.getString("pid"),
                    rs.getString("name"),
                    rs.getString("password")
                ));
            }
        }
        return students;
    }
    
    public static boolean addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (pid, name, password) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getPid());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getPassword());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public static boolean deleteStudent(String pid) throws SQLException {
        String sql = "DELETE FROM students WHERE pid = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, pid);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public static boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, password = ? WHERE pid = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getPassword());
            stmt.setString(3, student.getPid());
            
            return stmt.executeUpdate() > 0;
        }
    }
}
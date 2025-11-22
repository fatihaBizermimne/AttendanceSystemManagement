package dao;

import models.Teacher;
import java.sql.*;

public class TeacherDAO {
    
    public static Teacher getTeacherByPid(String pid) throws SQLException {
        String sql = "SELECT * FROM teachers WHERE pid = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, pid);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Teacher(
                    rs.getString("pid"),
                    rs.getString("name"),
                    rs.getString("password")
                );
            }
        }
        return null;
    }
}
package org.example.dao;

import org.example.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public boolean isAdmin(String userId) {
        return hasRole(userId, "ADMIN");
    }

    public boolean isJournalist(String userId) {
        return hasRole(userId, "JOURNALIST");
    }

    private boolean hasRole(String userId, String roleCode) {
        String sql = "SELECT 1 FROM users WHERE id = ? AND role = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, userId);
            ps.setString(2, roleCode);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public UserDTO getUserById(String id) {
        UserDTO user = null;
        String sql = "SELECT username, email, password, created_at, role FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new UserDTO();
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setCreated_at(rs.getString("created_at"));
                user.setRole(rs.getString("role"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

}

package org.example.dao;

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
        String sql = "SELECT 1 FROM user WHERE id = ? AND role = ?";

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
}

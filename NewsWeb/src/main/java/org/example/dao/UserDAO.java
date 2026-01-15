package org.example.dao;

import org.example.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public boolean checkUser(String username, String password) {

        String sql = """
            SELECT 1
            FROM users
            WHERE username = ?
              AND password = ?
            LIMIT 1
        """;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            rs = ps.executeQuery();

            // Nếu có ít nhất 1 record → login thành công
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            // Đóng tài nguyên theo thứ tự ngược lại
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkUsername(String username){
        String sql = """
            SELECT 1
            FROM users
            WHERE username = ?
            LIMIT 1
        """;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            // Đóng tài nguyên theo thứ tự ngược lại
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createUser(User user) {

        String sql = """
        INSERT INTO users (id, username, email, password, created_at, role)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getCreated_at());
            ps.setString(6, user.getRole());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


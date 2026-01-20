package org.example.dao;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class UserDAO {

    public String getPasswordByUsername(String username) {

        String sql = """
        SELECT password
        FROM users
        WHERE username = ?
        LIMIT 1
    """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("password"); // trả về String
            }

            return null; // không tìm thấy user

        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public void createUser(UserDTO userCreate) {

        String sql = """
        INSERT INTO users (id, username, email, password, created_at, role)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

        User user = new User();
        user.setUsername(userCreate.getUsername());
        user.setEmail(userCreate.getEmail());
        user.setPassword(userCreate.getPassword());
        user.setCreated_at(LocalDate.now().toString());
        user.setRole("user");

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


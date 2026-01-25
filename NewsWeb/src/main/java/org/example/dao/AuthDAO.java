package org.example.dao;

import org.example.dto.UserDTO;
import org.example.entity.Token;
import org.example.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AuthDAO {

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


    public boolean checkUsername(String username) {
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
        
                INSERT INTO users (id, username, email, password, created_at, role, isVerity)
        VALUES (?, ?, ?, ?, ?, ?, ?)
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
            ps.setBoolean(7, false);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isCheckVerity(String username) {
        String sql = "SELECT isVerity FROM user WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("isVerity");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // không tìm thấy user hoặc lỗi → coi như chưa verify
        return false;
    }

    public User getUserbyUserId(String userId) {

        String sql = "SELECT * FROM user WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);

            rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setCreated_at(rs.getString("created_at"));
                user.setRole(rs.getString("role"));
                user.setVerity(rs.getBoolean("isVerity"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public String getUserIdByUsername(String username) {

        String sql = "SELECT id FROM user WHERE username = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public void createToken(UserDTO userDTO) {

        String username = userDTO.getUsername();
        String id = this.getUserIdByUsername(username);
        User user = this.getUserbyUserId(id);

        // tạo token
        Token token = new Token();

        token.setUserId(user.getId());
        token.setEmail(user.getEmail());
        token.setUsername(user.getUsername());

        long iat = System.currentTimeMillis() / 1000; // epoch seconds
        long exp = iat + 5 * 60; // hết hạn sau 5 phút

        token.setIat((int) iat);
        token.setExp((int) exp);

        String sql = "INSERT INTO token (id, user_id, email, username, iat, exp) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, token.getId());
            ps.setString(2, token.getUserId());
            ps.setString(3, token.getEmail());
            ps.setString(4, token.getUsername());
            ps.setInt(5, token.getIat());
            ps.setInt(6, token.getExp());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

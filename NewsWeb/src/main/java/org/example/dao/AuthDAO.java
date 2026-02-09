package org.example.dao;

import org.example.dto.UserDTO;
import org.example.entity.Token;
import org.example.entity.User;
import org.example.utils.GenerateOtp;

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

    public String createUser(UserDTO userCreate) {

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
        return user.getId();
    }

    public boolean isCheckVerity(String username) {
        String sql = "SELECT isVerity FROM users WHERE username = ?";

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

    public String getUserIdByUsername(String username) {

        String sql = "SELECT id FROM users WHERE username = ?";

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

    public User getUserbyUserId(String userId) {

        String sql = "SELECT * FROM users WHERE id = ?";

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


    public String createOtp(String userId) {

        // tạo OTP 6 số
        String otpCode = GenerateOtp.genOtp();

        long iat = System.currentTimeMillis() / 1000; // epoch seconds
        long exp = iat + 5 * 60; // hết hạn sau 5 phút

        String otpId = java.util.UUID.randomUUID().toString();

        String sql = "INSERT INTO token (id, user_id, otp_code, iat, exp) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, otpId);
            ps.setString(2, userId);
            ps.setString(3, otpCode);
            ps.setLong(4, iat);
            ps.setLong(5, exp);

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

        // trả OTP để gửi email
        return otpCode;
    }

    public boolean verifyOtp(String userId, String otpInput) {

        String sql = "SELECT otp_code, exp FROM token "
                + "WHERE user_id = ? ORDER BY iat DESC LIMIT 1";

        long now = System.currentTimeMillis() / 1000; // epoch seconds

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);

            rs = ps.executeQuery();

            if (rs.next()) {
                String otpCode = rs.getString("otp_code");
                long exp = rs.getLong("exp");

                // kiểm tra OTP đúng và chưa hết hạn
                if (otpInput.equals(otpCode) && now <= exp) {
                    return true;
                }
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

        return false;
    }


    public boolean deleteToken(String userId) {

        String sql = "DELETE FROM token WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateUserVerified(String userId) {

        String sql = "UPDATE users SET is_verified = 1 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}

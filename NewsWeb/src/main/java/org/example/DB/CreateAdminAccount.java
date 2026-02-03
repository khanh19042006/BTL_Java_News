package org.example.DB;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.dao.DBConnection;
import org.example.entity.User;
import org.example.utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class CreateAdminAccount {

    public void createAdminAccount() {

        Dotenv dotenv = Dotenv.load();

        String username = dotenv.get("ADMIN_USERNAME");
        String email = dotenv.get("ADMIN_EMAIL");
        String password = dotenv.get("ADMIN_PASSWORD");
        String role = dotenv.get("ADMIN_ROLE");

        try (Connection conn = DBConnection.getConnection()) {

            // Kiểm tra đã có admin chưa
            String checkSql = "SELECT COUNT(*) FROM users WHERE role = ?";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, role);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Admin already exists. Skipping creation.");
                    return;
                }
            }

            // Nếu chưa có thì tạo mới
            String insertSql = """
                INSERT INTO users (id, username, email, password, created_at, role, isVerity)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

            User admin = new User();

            admin.setId("c505cc32-1ea9-47a2-b936-327aaf483db");
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setPassword(PasswordUtils.hash(password));
            admin.setCreated_at(LocalDate.now().toString());
            admin.setRole(role); // "admin"

            try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {

                insertPs.setString(1, admin.getId());
                insertPs.setString(2, admin.getUsername());
                insertPs.setString(3, admin.getEmail());
                insertPs.setString(4, admin.getPassword());
                insertPs.setString(5, admin.getCreated_at());
                insertPs.setString(6, admin.getRole());
                insertPs.setBoolean(7, true); // auto verify

                insertPs.executeUpdate();
                System.out.println("Admin account created successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

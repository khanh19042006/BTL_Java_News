package org.example.DB.CreateJSON.Create;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.dao.DBConnection;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CreateUsersJSON {

    public static void exportToJson(String outputPath) {

        String sql = """
                SELECT id, username, email, password,
                       created_at, role, isVerity
                FROM users
                """;

        List<UserData> list = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new UserData(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("created_at"),
                        rs.getString("role"),
                        rs.getBoolean("isVerity")
                ));
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(outputPath)) {
                gson.toJson(list, writer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class UserData {
        String id, username, email, password, created_at, role;
        boolean isVerity;

        public UserData(String id, String username, String email,
                        String password, String created_at,
                        String role, boolean isVerity) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.password = password;
            this.created_at = created_at;
            this.role = role;
            this.isVerity = isVerity;
        }
    }
}
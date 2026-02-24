package org.example.DB.AddTables.Add;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.dao.DBConnection;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

public class AddUsers {

    public static void importFromJson(String jsonPath) {

        String selectSql = """
                SELECT username, email, password,
                       created_at, role, isVerity
                FROM users
                WHERE id = ?
                """;

        String updateSql = """
                UPDATE users
                SET username = ?,
                    email = ?,
                    password = ?,
                    created_at = ?,
                    role = ?,
                    isVerity = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             FileReader reader = new FileReader(jsonPath);
             PreparedStatement selectPs = connection.prepareStatement(selectSql);
             PreparedStatement updatePs = connection.prepareStatement(updateSql)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<UserData>>() {}.getType();
            List<UserData> list = gson.fromJson(reader, listType);

            int updatedCount = 0;
            int skippedCount = 0;

            for (UserData item : list) {

                selectPs.setString(1, item.getId());
                ResultSet rs = selectPs.executeQuery();

                if (rs.next()) {

                    boolean isDifferent =
                            !Objects.equals(rs.getString("username"), item.getUsername()) ||
                                    !Objects.equals(rs.getString("email"), item.getEmail()) ||
                                    !Objects.equals(rs.getString("password"), item.getPassword()) ||
                                    !Objects.equals(rs.getString("created_at"), item.getCreated_at()) ||
                                    !Objects.equals(rs.getString("role"), item.getRole()) ||
                                    rs.getBoolean("isVerity") != item.getIsVerity();

                    if (isDifferent) {

                        updatePs.setString(1, item.getUsername());
                        updatePs.setString(2, item.getEmail());
                        updatePs.setString(3, item.getPassword());
                        updatePs.setString(4, item.getCreated_at());
                        updatePs.setString(5, item.getRole());
                        updatePs.setBoolean(6, item.getIsVerity());
                        updatePs.setString(7, item.getId());

                        updatePs.executeUpdate();
                        updatedCount++;
                    } else {
                        skippedCount++;
                    }
                }

                rs.close();
            }

            System.out.println("Updated Users: " + updatedCount);
            System.out.println("Skipped Users: " + skippedCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class UserData {

        private String id;
        private String username;
        private String email;
        private String password;
        private String created_at;
        private String role;
        private Boolean isVerity;

        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getCreated_at() { return created_at; }
        public String getRole() { return role; }
        public Boolean getIsVerity() { return isVerity; }
    }
}
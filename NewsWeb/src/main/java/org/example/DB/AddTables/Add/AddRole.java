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

public class AddRole {

    public static void importFromJson(String jsonPath) {

        String selectSql = """
                SELECT code, name
                FROM role
                WHERE id = ?
                """;

        String updateSql = """
                UPDATE role
                SET code = ?,
                    name = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             FileReader reader = new FileReader(jsonPath);
             PreparedStatement selectPs = connection.prepareStatement(selectSql);
             PreparedStatement updatePs = connection.prepareStatement(updateSql)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<RoleData>>() {}.getType();
            List<RoleData> list = gson.fromJson(reader, listType);

            int updatedCount = 0;
            int skippedCount = 0;

            for (RoleData item : list) {

                selectPs.setString(1, item.getId());
                ResultSet rs = selectPs.executeQuery();

                if (rs.next()) {

                    boolean isDifferent =
                            !Objects.equals(rs.getString("code"), item.getCode()) ||
                                    !Objects.equals(rs.getString("name"), item.getName());

                    if (isDifferent) {

                        updatePs.setString(1, item.getCode());
                        updatePs.setString(2, item.getName());
                        updatePs.setString(3, item.getId());

                        updatePs.executeUpdate();
                        updatedCount++;
                    } else {
                        skippedCount++;
                    }
                }

                rs.close();
            }

            System.out.println("Updated Role: " + updatedCount);
            System.out.println("Skipped Role: " + skippedCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class RoleData {

        private String id;
        private String code;
        private String name;

        public String getId() { return id; }
        public String getCode() { return code; }
        public String getName() { return name; }
    }
}
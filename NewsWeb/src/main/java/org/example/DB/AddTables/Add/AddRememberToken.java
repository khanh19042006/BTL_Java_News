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

public class AddRememberToken {

    public static void importFromJson(String jsonPath) {

        String selectSql = """
                SELECT user_id, created_at
                FROM remember_token
                WHERE id = ?
                """;

        String updateSql = """
                UPDATE remember_token
                SET user_id = ?,
                    created_at = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             FileReader reader = new FileReader(jsonPath);
             PreparedStatement selectPs = connection.prepareStatement(selectSql);
             PreparedStatement updatePs = connection.prepareStatement(updateSql)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<RememberTokenData>>() {}.getType();
            List<RememberTokenData> list = gson.fromJson(reader, listType);

            int updatedCount = 0;
            int skippedCount = 0;

            for (RememberTokenData item : list) {

                selectPs.setString(1, item.getId());
                ResultSet rs = selectPs.executeQuery();

                if (rs.next()) {

                    boolean isDifferent =
                            !Objects.equals(rs.getString("user_id"), item.getUser_id()) ||
                                    rs.getLong("created_at") != item.getCreated_at();

                    if (isDifferent) {

                        updatePs.setString(1, item.getUser_id());
                        updatePs.setLong(2, item.getCreated_at());
                        updatePs.setString(3, item.getId());

                        updatePs.executeUpdate();
                        updatedCount++;
                    } else {
                        skippedCount++;
                    }
                }

                rs.close();
            }

            System.out.println("Updated Remember Token: " + updatedCount);
            System.out.println("Skipped Remember Token: " + skippedCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class RememberTokenData {

        private String id;
        private String user_id;
        private long created_at;

        public String getId() { return id; }
        public String getUser_id() { return user_id; }
        public long getCreated_at() { return created_at; }
    }
}
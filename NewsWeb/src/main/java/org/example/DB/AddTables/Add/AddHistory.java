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

public class AddHistory {

    public static void importFromJson(String jsonPath) {

        String selectSql = "SELECT user_id, news_id, read_at FROM history WHERE id = ?";
        String updateSql = """
                UPDATE history
                SET user_id = ?, news_id = ?, read_at = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             FileReader reader = new FileReader(jsonPath);
             PreparedStatement selectPs = connection.prepareStatement(selectSql);
             PreparedStatement updatePs = connection.prepareStatement(updateSql)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<HistoryData>>() {}.getType();
            List<HistoryData> list = gson.fromJson(reader, listType);

            int updatedCount = 0;
            int skippedCount = 0;

            for (HistoryData item : list) {

                selectPs.setString(1, item.getId());
                ResultSet rs = selectPs.executeQuery();

                if (rs.next()) {

                    boolean isDifferent =
                            !Objects.equals(rs.getString("user_id"), item.getUserId()) ||
                                    !Objects.equals(rs.getString("news_id"), item.getNewsId()) ||
                                    !Objects.equals(rs.getString("read_at"), item.getReadAt());

                    if (isDifferent) {

                        updatePs.setString(1, item.getUserId());
                        updatePs.setString(2, item.getNewsId());
                        updatePs.setString(3, item.getReadAt());
                        updatePs.setString(4, item.getId());

                        updatePs.executeUpdate();
                        updatedCount++;
                    } else {
                        skippedCount++;
                    }
                }

                rs.close();
            }

            System.out.println("Updated History: " + updatedCount);
            System.out.println("Skipped History: " + skippedCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class HistoryData {

        private String id;
        private String user_id;
        private String news_id;
        private String read_at;

        public String getId() { return id; }
        public String getUserId() { return user_id; }
        public String getNewsId() { return news_id; }
        public String getReadAt() { return read_at; }
    }
}
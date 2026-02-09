package org.example.DB;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.dao.DBConnection;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.List;
import java.util.Objects;

public class AddContent {

    public static void importFromJson(String jsonPath) {

        String selectSql = "SELECT content FROM news WHERE id = ?";
        String updateSql = "UPDATE news SET content = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             FileReader reader = new FileReader(jsonPath);
             PreparedStatement selectPs = connection.prepareStatement(selectSql);
             PreparedStatement updatePs = connection.prepareStatement(updateSql)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<NewsContent>>() {}.getType();
            List<NewsContent> list = gson.fromJson(reader, listType);

            int updatedCount = 0;
            int skippedCount = 0;

            for (NewsContent item : list) {

                // Lấy content hiện tại trong DB
                selectPs.setString(1, item.getId());
                ResultSet rs = selectPs.executeQuery();

                if (rs.next()) {
                    String dbContent = rs.getString("content");

                    // So sánh
                    if (!Objects.equals(dbContent, item.getContent())) {

                        // Nếu khác thì update
                        updatePs.setString(1, item.getContent());
                        updatePs.setString(2, item.getId());
                        updatePs.executeUpdate();

                        updatedCount++;
                    } else {
                        skippedCount++;
                    }
                }

                rs.close();
            }

            System.out.println("Updated: " + updatedCount);
            System.out.println("Skipped (giống nhau): " + skippedCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class NewsContent {
        private String id;
        private String content;

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }
    }
}

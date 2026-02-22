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

public class AddComments {

    public static void importFromJson(String jsonPath) {

        String selectSql = "SELECT content, author_id, news_id, time_up, parent_id FROM comments WHERE id = ?";
        String updateSql = """
                UPDATE comments
                SET content = ?, author_id = ?, news_id = ?, time_up = ?, parent_id = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             FileReader reader = new FileReader(jsonPath);
             PreparedStatement selectPs = connection.prepareStatement(selectSql);
             PreparedStatement updatePs = connection.prepareStatement(updateSql)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<CommentData>>() {}.getType();
            List<CommentData> list = gson.fromJson(reader, listType);

            int updatedCount = 0;
            int skippedCount = 0;

            for (CommentData item : list) {

                selectPs.setString(1, item.getId());
                ResultSet rs = selectPs.executeQuery();

                if (rs.next()) {

                    boolean isDifferent =
                            !Objects.equals(rs.getString("content"), item.getContent()) ||
                                    !Objects.equals(rs.getString("author_id"), item.getAuthorId()) ||
                                    !Objects.equals(rs.getString("news_id"), item.getNewsId()) ||
                                    !Objects.equals(rs.getString("time_up"), item.getTimeUp()) ||
                                    !Objects.equals(rs.getString("parent_id"), item.getParentId());

                    if (isDifferent) {

                        updatePs.setString(1, item.getContent());
                        updatePs.setString(2, item.getAuthorId());
                        updatePs.setString(3, item.getNewsId());
                        updatePs.setString(4, item.getTimeUp());
                        updatePs.setString(5, item.getParentId());
                        updatePs.setString(6, item.getId());

                        updatePs.executeUpdate();
                        updatedCount++;
                    } else {
                        skippedCount++;
                    }
                }

                rs.close();
            }

            System.out.println("Updated Comments: " + updatedCount);
            System.out.println("Skipped Comments: " + skippedCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class CommentData {

        private String id;
        private String content;
        private String author_id;
        private String news_id;
        private String time_up;
        private String parent_id;

        public String getId() { return id; }
        public String getContent() { return content; }
        public String getAuthorId() { return author_id; }
        public String getNewsId() { return news_id; }
        public String getTimeUp() { return time_up; }
        public String getParentId() { return parent_id; }
    }
}
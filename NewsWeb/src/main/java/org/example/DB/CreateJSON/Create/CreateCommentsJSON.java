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

public class CreateCommentsJSON {

    public static void exportToJson(String outputPath) {

        String sql = """
                SELECT id, content, author_id, news_id, time_up, parent_id
                FROM comments
                """;

        List<CommentData> list = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new CommentData(
                        rs.getString("id"),
                        rs.getString("content"),
                        rs.getString("author_id"),
                        rs.getString("news_id"),
                        rs.getString("time_up"),
                        rs.getString("parent_id")
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

    private static class CommentData {
        String id, content, author_id, news_id, time_up, parent_id;

        public CommentData(String id, String content, String author_id,
                           String news_id, String time_up, String parent_id) {
            this.id = id;
            this.content = content;
            this.author_id = author_id;
            this.news_id = news_id;
            this.time_up = time_up;
            this.parent_id = parent_id;
        }
    }
}
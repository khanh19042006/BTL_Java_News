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

public class CreateHistoryJSON {

    public static void exportToJson(String outputPath) {

        String sql = "SELECT id, user_id, news_id, read_at FROM history";

        List<HistoryData> list = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new HistoryData(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("news_id"),
                        rs.getString("read_at")
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

    private static class HistoryData {
        String id, user_id, news_id, read_at;

        public HistoryData(String id, String user_id, String news_id, String read_at) {
            this.id = id;
            this.user_id = user_id;
            this.news_id = news_id;
            this.read_at = read_at;
        }
    }
}
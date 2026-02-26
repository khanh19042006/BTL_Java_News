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

public class CreateRememberTokenJSON {

    public static void exportToJson(String outputPath) {

        String sql = "SELECT id, user_id, created_at FROM remember_token";

        List<RememberTokenData> list = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new RememberTokenData(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getLong("created_at")
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

    private static class RememberTokenData {
        String id, user_id;
        long created_at;

        public RememberTokenData(String id, String user_id, long created_at) {
            this.id = id;
            this.user_id = user_id;
            this.created_at = created_at;
        }
    }
}
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

public class CreateTokenJSON {

    public static void exportToJson(String outputPath) {

        String sql = "SELECT id, user_id, otp_code, iat, exp FROM token";

        List<TokenData> list = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new TokenData(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("otp_code"),
                        rs.getLong("iat"),
                        rs.getLong("exp")
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

    private static class TokenData {
        String id, user_id, otp_code;
        long iat, exp;

        public TokenData(String id, String user_id,
                         String otp_code, long iat, long exp) {
            this.id = id;
            this.user_id = user_id;
            this.otp_code = otp_code;
            this.iat = iat;
            this.exp = exp;
        }
    }
}
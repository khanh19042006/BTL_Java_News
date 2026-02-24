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

public class AddToken {

    public static void importFromJson(String jsonPath) {

        String selectSql = """
                SELECT user_id, otp_code, iat, exp
                FROM token
                WHERE id = ?
                """;

        String updateSql = """
                UPDATE token
                SET user_id = ?,
                    otp_code = ?,
                    iat = ?,
                    exp = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             FileReader reader = new FileReader(jsonPath);
             PreparedStatement selectPs = connection.prepareStatement(selectSql);
             PreparedStatement updatePs = connection.prepareStatement(updateSql)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<TokenData>>() {}.getType();
            List<TokenData> list = gson.fromJson(reader, listType);

            int updatedCount = 0;
            int skippedCount = 0;

            for (TokenData item : list) {

                selectPs.setString(1, item.getId());
                ResultSet rs = selectPs.executeQuery();

                if (rs.next()) {

                    boolean isDifferent =
                            !Objects.equals(rs.getString("user_id"), item.getUser_id()) ||
                                    !Objects.equals(rs.getString("otp_code"), item.getOtp_code()) ||
                                    rs.getLong("iat") != item.getIat() ||
                                    rs.getLong("exp") != item.getExp();

                    if (isDifferent) {

                        updatePs.setString(1, item.getUser_id());
                        updatePs.setString(2, item.getOtp_code());
                        updatePs.setLong(3, item.getIat());
                        updatePs.setLong(4, item.getExp());
                        updatePs.setString(5, item.getId());

                        updatePs.executeUpdate();
                        updatedCount++;
                    } else {
                        skippedCount++;
                    }
                }

                rs.close();
            }

            System.out.println("Updated Token: " + updatedCount);
            System.out.println("Skipped Token: " + skippedCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class TokenData {

        private String id;
        private String user_id;
        private String otp_code;
        private long iat;
        private long exp;

        public String getId() { return id; }
        public String getUser_id() { return user_id; }
        public String getOtp_code() { return otp_code; }
        public long getIat() { return iat; }
        public long getExp() { return exp; }
    }
}
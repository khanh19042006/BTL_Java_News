package org.example.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImportCategory {

    public void importCategory(Connection conn) {

        if (hasData(conn)) {
            System.out.println("Category table already has data. Skip import.");
            return;
        }

        String sql =
                "INSERT INTO category (id, name) " +
                        "SELECT DISTINCT " +
                        "    LOWER(category) AS id, " +
                        "    category AS name " +
                        "FROM news " +
                        "WHERE category IS NOT NULL";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int rows = ps.executeUpdate();
            System.out.println("Inserted categories: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // kiểm tra category có dữ liệu hay chưa
    public boolean hasData(Connection conn) {
        String sql = "SELECT 1 FROM category LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

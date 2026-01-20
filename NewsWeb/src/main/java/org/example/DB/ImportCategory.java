package org.example.DB;

import org.example.entity.Category;

import java.sql.*;

public class ImportCategory {

    public void importCategory(Connection conn) {

        if (hasData(conn)) {
            System.out.println("Category table already has data. Skip import.");
            return;
        }

        String selectSql =
                "SELECT DISTINCT category " +
                        "FROM news " +
                        "WHERE category IS NOT NULL AND TRIM(category) <> ''";

        String insertSql =
                "INSERT INTO category (id, code, name) VALUES (?, ?, ?)";

        try (PreparedStatement selectPs = conn.prepareStatement(selectSql);
             ResultSet rs = selectPs.executeQuery();
             PreparedStatement insertPs = conn.prepareStatement(insertSql)) {

            int count = 0;

            while (rs.next()) {
                String rawCategory = rs.getString("category").trim();
                String code = rawCategory.toLowerCase();

                Category category = new Category(); // ⭐ sinh UUID ở đây
                category.setCode(code);
                category.setName(rawCategory);

                insertPs.setString(1, category.getId());   // id
                insertPs.setString(2, category.getCode()); // code
                insertPs.setString(3, category.getName()); // name
                insertPs.addBatch();

                count++;
            }

            insertPs.executeBatch();
            System.out.println("Inserted categories: " + count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // kiểm tra bảng category đã có dữ liệu hay chưa
    public boolean hasData(Connection conn) {
        String sql = "SELECT 1 FROM category LIMIT 1";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // nếu có ít nhất 1 dòng => bảng có dữ liệu
            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // đóng ResultSet
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // đóng PreparedStatement
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // không có dòng nào
        return false;
    }
}


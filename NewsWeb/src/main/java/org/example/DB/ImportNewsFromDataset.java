package org.example.DB;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.entity.News;
import org.example.service.enumCRUD.SQLQuery;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImportNewsFromDataset {

    private static final Dotenv dotenv = Dotenv.load();
    private final Gson gson = new Gson();
    private static final int BATCH_SIZE = 2000;

    //Chuyển dữ liệu t file .json vòa db
    public void importDataFromJsonFileToDatabase(String url, Connection connection) {
        int count = 0;

        File file = new File(url);
        String fileName = file.getName();

        try {
            if (isCheckTable(connection, "news")) return;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Đang import file: " + fileName);

        String sql = SQLQuery.INSERT_NEWS.getQuery();

        boolean oldAutoCommit = true;

        try {
            oldAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (BufferedReader br = new BufferedReader(new FileReader(file));
                 PreparedStatement stmt = connection.prepareStatement(sql)) {

                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    try {
                        News news = gson.fromJson(line, News.class);

                        stmt.setString(1, news.getId());
                        stmt.setString(2, news.getLink());
                        stmt.setString(3, news.getHeadline());
                        stmt.setString(4, news.getCategory());
                        stmt.setString(5, news.getShort_description());
                        stmt.setString(6, news.getAuthors());
                        stmt.setString(7, news.getDate());
                        stmt.setInt(8, news.getViews());
                        stmt.addBatch();

                        count++;

                        if (count % BATCH_SIZE == 0) {
                            stmt.executeBatch();
                        }

                    } catch (Exception e) {
                        System.err.println("Parse error: " + line);
                        e.printStackTrace();
                    }
                }

                stmt.executeBatch();
                connection.commit();

                System.out.println("Import xong " + count + " bản ghi từ file: " + fileName);
            }

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();

        } finally {
            try {
                connection.setAutoCommit(oldAutoCommit);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    //Kiểm tra Bảng này đã được insert vào db chưa để tránh insert nhiều lần
    public boolean isCheckTable(Connection connection, String tableName) throws SQLException {

        // 1. Validate tên table
        if (!tableName.matches("[a-zA-Z0-9_]+")) {
            throw new SQLException("Invalid table name: " + tableName);
        }

        // 2. Query kiểm tra table có record không
        String sql = "SELECT 1 FROM " + tableName + " LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Nếu có ít nhất 1 dòng → table có record
            return rs.next();
        }
    }
}

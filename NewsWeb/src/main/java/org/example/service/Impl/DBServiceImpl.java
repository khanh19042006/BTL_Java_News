package org.example.service.Impl;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.entity.CheckTable;
import org.example.entity.News;
import org.example.service.DBService;
import org.example.service.enumCRUD.SQLQuery;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBServiceImpl implements DBService {

    private static final Dotenv dotenv = Dotenv.load();
    private final Gson gson = new Gson();
    private static final int BATCH_SIZE = 2000;

    //Chuyển dữ liệu t file .json vòa db
    @Override
    public void importDataFromJsonFileToDatabase(String url, Connection connection) {
        int count = 0;

        File file = new File(url);
        String fileName = file.getName();

//        try {
//            if (isCheckTable(connection, fileName)) return;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

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
    @Override
    public boolean isCheckTable(Connection connection, String name) throws SQLException {
        // 1. Kiểm tra xem đã tồn tại chưa
        String selectSql = "SELECT name FROM checkTable WHERE name = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setString(1, name);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    // đã tồn tại
                    System.out.println("Tồn tại rồi " + name);
                    return true;
                }
            }
        }

        //Nếu chưa tồn tại, thêm record mới
        CheckTable newRecord = new CheckTable(name, true);
        String insertSql = "INSERT INTO checkTable(id, name, check_flag) VALUES (?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
            insertStmt.setString(1, newRecord.getId());
            insertStmt.setString(2, newRecord.getName());
            insertStmt.setBoolean(3, newRecord.isCheck_flag()); // status = true
            insertStmt.executeUpdate();
        }

        // Chưa tồn tại
        return false;
    }
}

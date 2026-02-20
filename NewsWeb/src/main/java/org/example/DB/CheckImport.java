package org.example.DB;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.dao.DBConnection;

import java.sql.Connection;

public class CheckImport {

    private static final Dotenv dotenv = Dotenv.load();
    private final CreateAdminAccount createAdminAccount = new CreateAdminAccount();

    public void importNews() {
        try (Connection conn = DBConnection.getConnection()) {
            ImportNewsFromDataset newsImporter = new ImportNewsFromDataset();
            newsImporter.importDataFromJsonFileToDatabase(
                    dotenv.get("DB_JSON_URL"), conn
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importCategory() {
        try (Connection conn = DBConnection.getConnection()) {
            ImportCategory categoryImporter = new ImportCategory();
            categoryImporter.importCategory(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // hàm tổng gọi cả 2 (optional)
    public void checkImport() {
        importNews();
        importCategory();
        this.createAdminAccount.createAdminAccount();
    }

}

package org.example.service;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBService {
    public void importDataFromJsonFileToDatabase(String url, Connection connection);
    public boolean isCheckTable(Connection connection, String name) throws SQLException;
}

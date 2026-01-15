package org.example.service.enumCRUD;

public enum SQLQuery {
    INSERT_NEWS("INSERT IGNORE INTO news(id, link, headline, category, short_description, authors, date, view) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"),
    // Có thể thêm các query khác ở đây
    ;
    private final String query;

    SQLQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

}

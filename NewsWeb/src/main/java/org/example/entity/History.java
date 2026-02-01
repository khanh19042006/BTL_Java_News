package org.example.entity;

import java.util.UUID;

public class History {
    private String id;
    private String userId;
    private String newsId;
    private String readAt;

    public History(String userId, String newsId, String readAt) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.newsId = newsId;
        this.readAt = readAt;
    }

    public History() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getReadAt() {
        return readAt;
    }

    public void setReadAt(String readAt) {
        this.readAt = readAt;
    }
}

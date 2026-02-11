package org.example.entity;

import java.util.UUID;

public class RememberAuth {
    private String id;
    private String userId;
    private long createAt;

    public RememberAuth() {
        this.id = UUID.randomUUID().toString();
    }

    public RememberAuth(String userId, long createAt) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.createAt = createAt;
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

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
}

package org.example.entity;

import java.util.UUID;

public class RoleUpgradeRequest {
    private String id;
    private String userId;
    private String createdAt;
    private boolean isAccept;

    public RoleUpgradeRequest() {
        this.id = UUID.randomUUID().toString();
    }

    public RoleUpgradeRequest(String userId, String createdAt, boolean isAccept) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.createdAt = createdAt;
        this.isAccept = isAccept;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean accept) {
        isAccept = accept;
    }
}

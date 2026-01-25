package org.example.entity;

import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String created_at;
    private String role;    //role_code
    private boolean isVerity;

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public User(String username, String email, String password, String created_at, String role, boolean isVerity) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.role = role;
        this.isVerity = isVerity;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isVerity() {
        return isVerity;
    }

    public void setVerity(boolean verity) {
        isVerity = verity;
    }
}

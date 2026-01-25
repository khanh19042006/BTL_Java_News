package org.example.entity;

import java.util.UUID;

public class Token {
    private String id;
    private String userId;
    private String email;
    private String username;
    private int iat;
    private int exp;

    public Token() {
        this.id = UUID.randomUUID().toString();
    }

    public Token(String userId, String email, String username, int iat, int exp) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.iat = iat;
        this.exp = exp;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIat() {
        return iat;
    }

    public void setIat(int iat) {
        this.iat = iat;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}

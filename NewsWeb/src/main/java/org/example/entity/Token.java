package org.example.entity;

import java.util.UUID;

public class Token {
    private String id;
    private String userId;
    private String otp;
    private int iat;
    private int exp;

    public Token() {
        this.id = UUID.randomUUID().toString();
    }

    public Token(String userId, String otp, int iat, int exp) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.otp = otp;
        this.iat = iat;
        this.exp = exp;
    }

    public String getId() {
        return id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

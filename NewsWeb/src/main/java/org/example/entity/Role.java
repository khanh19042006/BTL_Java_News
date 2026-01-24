package org.example.entity;

import java.util.UUID;

public class Role {
    private String id;
    private String code;
    private String name;

    public Role(String code, String name) {
        this.id = UUID.randomUUID().toString();
        this.code = code;
        this.name = name;
    }

    public Role() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

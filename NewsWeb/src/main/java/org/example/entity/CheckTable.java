package org.example.entity;

import java.util.UUID;

public class CheckTable {
    private String id;
    private String name;
    private boolean check_flag;

    public CheckTable() {
        this.id = UUID.randomUUID().toString();
    }

    public CheckTable(String name, boolean check_flag) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.check_flag = check_flag;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck_flag() {
        return check_flag;
    }

    public void setCheck_flag(boolean check_flag) {
        this.check_flag = check_flag;
    }
}

package com.sykun.baizhimall.entity;

public class Role {

    private String id;
    private String username;

    public Role() {
    }

    public Role(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

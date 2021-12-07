package com.wgcloud.entity;

import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginSet extends BaseEntity{
    private String username;

    private String password;

    private String reghts_id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReghts_id() {
        return reghts_id;
    }

    public void setReghts_id(String reghts_id) {
        this.reghts_id = reghts_id;
    }
}

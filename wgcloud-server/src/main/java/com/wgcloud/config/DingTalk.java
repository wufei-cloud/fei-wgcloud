package com.wgcloud.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class DingTalk {
    private String title;
    private String text;
    private String userid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

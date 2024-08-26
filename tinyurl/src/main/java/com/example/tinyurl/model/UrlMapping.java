package com.example.tinyurl.model;

import java.io.Serializable;

public class UrlMapping implements Serializable{

    private String id;
    private String originalUrl;

    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}

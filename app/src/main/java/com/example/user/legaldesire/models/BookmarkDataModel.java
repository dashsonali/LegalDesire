package com.example.user.legaldesire.models;

public class BookmarkDataModel {
    String key,link;

    public BookmarkDataModel(String key, String link) {
        this.key = key;
        this.link = link;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

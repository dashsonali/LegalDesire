package com.example.user.legaldesire.models;

public class BookmarkDataModel {
    String key,link,title;

    public BookmarkDataModel(String key, String link,String title) {
        this.key = key;
        this.link = link;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

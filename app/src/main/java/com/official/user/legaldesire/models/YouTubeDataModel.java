package com.official.user.legaldesire.models;

public class YouTubeDataModel {
    String title ="";
    String des ="";
    String thumb ="";
    String publishedAt ="";
    String video_id;

    public YouTubeDataModel(String title, String des, String thumb, String publishedAt,String video_id) {
        this.title = title;
        this.des = des;
        this.thumb = thumb;
        this.publishedAt = publishedAt;
        this.video_id = video_id;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}

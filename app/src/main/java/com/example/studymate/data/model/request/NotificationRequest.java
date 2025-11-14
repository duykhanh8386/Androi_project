package com.example.studymate.data.model.request;

import com.google.gson.annotations.SerializedName;

public class NotificationRequest {

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    public NotificationRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
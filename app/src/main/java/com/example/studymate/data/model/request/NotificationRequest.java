package com.example.studymate.data.model.request;


/**
 * POJO này đại diện cho Body JSON
 * được gửi đi khi tạo một thông báo mới.
 * (Dựa trên fragment_notification_create.xml)
 */
public class NotificationRequest {

    private String title;
    private String content;

    public NotificationRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Getters (cần cho Retrofit)
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
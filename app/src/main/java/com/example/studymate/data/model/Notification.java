package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("notificationId")
    private int notificationId;

    @SerializedName("notificationTitle")
    private String notificationTitle;

    @SerializedName("notificationContent")
    private String notificationContent;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("class")
    private StudyClass studyClass;

    @SerializedName("teacher")
    private User teacher;

    public Notification(int notificationId, String notificationTitle, String notificationContent, String createdAt) {
        this.notificationId = notificationId;
        this.notificationTitle = notificationTitle;
        this.notificationContent = notificationContent;
        this.createdAt = createdAt;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public StudyClass getStudyClass() {
        return studyClass;
    }

    public void setStudyClass(StudyClass studyClass) {
        this.studyClass = studyClass;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}
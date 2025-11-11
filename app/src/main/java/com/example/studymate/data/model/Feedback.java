package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Feedback {

    @SerializedName("feedbackId")
    private int feedbackId;

    @SerializedName("feedbackContent")
    private String feedbackContent;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("isRead")
    private String isRead;

    @SerializedName("class")
    private StudyClass studyClass;

    @SerializedName("sender")
    private User sender;

    public Feedback(int feedbackId, String feedbackContent, Date createdAt, String isRead, StudyClass studyClass, User sender) {
        this.feedbackId = feedbackId;
        this.feedbackContent = feedbackContent;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.studyClass = studyClass;
        this.sender = sender;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public StudyClass getStudyClass() {
        return studyClass;
    }

    public void setStudyClass(StudyClass studyClass) {
        this.studyClass = studyClass;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
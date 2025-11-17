package com.example.studymate.data.model.request;

import com.google.gson.annotations.SerializedName;

public class FeedbackRequest {

    @SerializedName("classId")
    private int classId;

    @SerializedName("feedbackContent")
    private String feedbackContent;

    @SerializedName("receiverId")
    private Long receiverId; // (ID của người nhận tin nhắn)

    public FeedbackRequest(int classId, String feedbackContent, Long receiverId) {
        this.classId = classId;
        this.feedbackContent = feedbackContent;
        this.receiverId = receiverId;
    }

    // --- (Getters / Setters) ---

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
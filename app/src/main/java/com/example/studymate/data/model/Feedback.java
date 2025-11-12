package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Feedback {

    @SerializedName("feedbackId")
    private int feedbackId;

    @SerializedName("content")
    private String feedbackContent;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("senderId")
    private Long senderId;

    @SerializedName("senderName")
    private String senderName;

    @SerializedName("receiverId")
    private Long receiverId;

    @SerializedName("receiverName")
    private String receiverName;

    @SerializedName("conversationId")
    private String conversationId;

    @SerializedName("read")
    private boolean isRead;

    public Feedback() {
    }

    public Feedback(int feedbackId, String feedbackContent, String createdAt, Long senderId, String senderName, Long receiverId, String receiverName, String conversationId, boolean isRead) {
        this.feedbackId = feedbackId;
        this.feedbackContent = feedbackContent;
        this.createdAt = createdAt;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.conversationId = conversationId;
        this.isRead = isRead;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
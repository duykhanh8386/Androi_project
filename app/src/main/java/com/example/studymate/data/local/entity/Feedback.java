package com.example.studymate.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "feedbacks")
public class Feedback {
    @PrimaryKey(autoGenerate = true)
    private int feedbackID;
    private int classID;
    private int senderID;
    private String content;
    private Date sentAt;
    private boolean isRead;

    public Feedback(int feedbackID, int classID, int senderID, String content, Date sentAt, boolean isRead) {
        this.feedbackID = feedbackID;
        this.classID = classID;
        this.senderID = senderID;
        this.content = content;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }

    public int getFeedbackID() { return feedbackID; }
    public void setFeedbackID(int feedbackID) { this.feedbackID = feedbackID; }
    public int getClassID() { return classID; }
    public void setClassID(int classID) { this.classID = classID; }
    public int getSenderID() { return senderID; }
    public void setSenderID(int senderID) { this.senderID = senderID; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getSentAt() { return sentAt; }
    public void setSentAt(Date sentAt) { this.sentAt = sentAt; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}

package com.example.studymate.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "notifications")
public class Notification {
    @PrimaryKey(autoGenerate = true)
    private int notificationID;
    private int classID;
    private String title;
    private String content;
    private Date sentAt;
    private int teacherID;

    public Notification(int notificationID, int classID, String title, String content, Date sentAt, int teacherID) {
        this.notificationID = notificationID;
        this.classID = classID;
        this.title = title;
        this.content = content;
        this.sentAt = sentAt;
        this.teacherID = teacherID;
    }

    public int getNotificationID() { return notificationID; }
    public void setNotificationID(int notificationID) { this.notificationID = notificationID; }
    public int getClassID() { return classID; }
    public void setClassID(int classID) { this.classID = classID; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getSentAt() { return sentAt; }
    public void setSentAt(Date sentAt) { this.sentAt = sentAt; }
    public int getTeacherID() { return teacherID; }
    public void setTeacherID(int teacherID) { this.teacherID = teacherID; }
}

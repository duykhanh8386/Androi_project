package com.example.studymate.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "feedback")
public class Feedback {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long classId;
    public long senderId;

    @NonNull public String message;
    public long createdAt;
    public boolean isRead;
    public boolean isTeacherReply;
}

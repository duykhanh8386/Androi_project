package com.example.studymate.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification")
public class Notification {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long classId;
    @NonNull public String title;
    @NonNull public String content;
    public long createdAt;
    public long senderId;
}

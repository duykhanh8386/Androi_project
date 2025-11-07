package com.example.studymate.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String username;

    public String passwordHash; // SHA-256("123456") khi seed
    public String role;         // ADMIN | TEACHER | STUDENT
    public String status;       // ACTIVE | INACTIVE
    public boolean disabled;    // true nếu bị vô hiệu hóa

    public String fullName;
    public String email;
    public String phone;
}

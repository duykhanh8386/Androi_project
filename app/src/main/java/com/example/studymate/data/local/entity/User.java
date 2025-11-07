package com.example.studymate.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull @ColumnInfo(index = true)
    public String username;

    @NonNull
    public String passwordHash;

    @NonNull
    public String role; // ADMIN, TEACHER, STUDENT

    @NonNull
    public String status; // ACTIVE | INACTIVE (session)

    public boolean disabled; // true => account locked

    public String fullName;
    public String email;
    public String phone;
}

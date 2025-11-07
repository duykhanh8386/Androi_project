package com.example.studymate.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "class")
public class ClassRoom {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String code;     // M101 ...
    public String name;     // Toán 10A1 ...
    public int year;        // 2025
    public int maxSize;     // 45
    public long teacherId;  // id user role TEACHER
}

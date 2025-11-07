package com.example.studymate.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "class")
public class ClassRoom {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull @ColumnInfo(index = true)
    public String code; // unique visible code

    public String name;
    public int year;
    public int maxSize;
    public long teacherId;
}

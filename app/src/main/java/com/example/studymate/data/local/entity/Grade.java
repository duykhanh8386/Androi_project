package com.example.studymate.data.local.entity;

import androidx.room.Entity;

@Entity(tableName = "grade", primaryKeys = {"classId","studentId"})
public class Grade {
    public long classId;
    public long studentId;
    public float quiz;
    public float mid;
    public float fin;
}

package com.example.studymate.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "student_class", primaryKeys = {"classId","studentId"})
public class StudentClass {
    public long classId;
    public long studentId;
    @NonNull public String status; // MEMBER | PENDING | REJECTED
}

package com.example.studymate.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "classes")
public class Class {
    @PrimaryKey(autoGenerate = true)
    private int classID;
    private String className;
    private String description;
    private String joinCode;
    private int teacherID;

    public Class(int classID, String className, String description, String joinCode, int teacherID) {
        this.classID = classID;
        this.className = className;
        this.description = description;
        this.joinCode = joinCode;
        this.teacherID = teacherID;
    }

    public int getClassID() { return classID; }
    public void setClassID(int classID) { this.classID = classID; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getJoinCode() { return joinCode; }
    public void setJoinCode(String joinCode) { this.joinCode = joinCode; }
    public int getTeacherID() { return teacherID; }
    public void setTeacherID(int teacherID) { this.teacherID = teacherID; }
}

package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class StudyClass {

    @SerializedName("classId")
    private int classId;

    @SerializedName("className")
    private String className;

    @SerializedName("classJoinCode")
    private String classJoinCode;

    @SerializedName("classTime")
    private String classTime;

    @SerializedName("createdAt")
    private Date createdAt; // Gson có thể tự parse Date nếu định dạng chuẩn (ISO 8601)

    @SerializedName("modifiedAt")
    private Date modifiedAt;

    @SerializedName("teacher")
    private User teacher;

    public StudyClass() {

    }

    public StudyClass(int classId, String className, String classTime) {
        this.classId = classId;
        this.className = className;
        this.classTime = classTime;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassJoinCode() {
        return classJoinCode;
    }

    public void setClassJoinCode(String classJoinCode) {
        this.classJoinCode = classJoinCode;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}
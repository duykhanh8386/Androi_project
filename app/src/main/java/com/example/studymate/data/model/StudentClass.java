package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

public class StudentClass {

    @SerializedName("studentClassId")
    private int studentClassId;

    @SerializedName("username")
    private String username;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("joinedAt")
    private String joinedAt;

    @SerializedName("leftAt")
    private String leftAt;

    @SerializedName("status")
    private String status;

    public StudentClass() {
    }

    public StudentClass(int studentClassId, String username, String fullName, String joinedAt, String leftAt, String status) {
        this.studentClassId = studentClassId;
        this.username = username;
        this.fullName = fullName;
        this.joinedAt = joinedAt;
        this.leftAt = leftAt;
        this.status = status;
    }

    public int getStudentClassId() {
        return studentClassId;
    }

    public void setStudentClassId(int studentClassId) {
        this.studentClassId = studentClassId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(String leftAt) {
        this.leftAt = leftAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Grade {

    @SerializedName("gradeId")
    private int gradeId;

    @SerializedName("gradeType")
    private String gradeType;

    @SerializedName("score")
    private double score;

    @SerializedName("modified_at")
    private Date modifiedAt;

    @SerializedName("student")
    private User student;

    @SerializedName("class")
    private StudyClass studyClass;

    public Grade(int gradeId, String gradeType, double score) {
        this.gradeId = gradeId;
        this.score = score;
        this.gradeType = gradeType;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public StudyClass getStudyClass() {
        return studyClass;
    }

    public void setStudyClass(StudyClass studyClass) {
        this.studyClass = studyClass;
    }
}
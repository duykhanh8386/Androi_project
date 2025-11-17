package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

public class Grade {

    @SerializedName("gradeId")
    private int gradeId;

    @SerializedName("gradeType")
    private String gradeType;

    @SerializedName("score")
    private Double score;

    @SerializedName("modifiedAt")
    private String modifiedAt;

    @SerializedName("studentId")
    private Long studentId;

    @SerializedName("classId")
    private Long classId;

    public Grade(int gradeId, String gradeType, Double score) {
        this.gradeId = gradeId;
        this.score = score;
        this.gradeType = gradeType;
    }

    public Grade(int gradeId, String gradeType, Double score, String modifiedAt, Long studentId, Long classId) {
        this.gradeId = gradeId;
        this.gradeType = gradeType;
        this.score = score;
        this.modifiedAt = modifiedAt;
        this.studentId = studentId;
        this.classId = classId;
    }

    public Grade() {
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}
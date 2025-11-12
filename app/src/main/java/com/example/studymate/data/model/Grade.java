package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
// ⭐️ XÓA: import java.util.Date;

public class Grade {

    @SerializedName("gradeId")
    private int gradeId;

    @SerializedName("gradeType")
    private String gradeType;

    // ⭐️ SỬA LẠI:
    // JSON trả về 8.00 (một con số), không phải "8.00" (một chuỗi)
    // Dùng Double (hoặc BigDecimal) để khớp
    @SerializedName("score")
    private Double score; // Sửa từ String -> Double

    @SerializedName("modifiedAt")
    private String modifiedAt;

    @SerializedName("studentId")
    private Long studentId;

    @SerializedName("classId")
    private Long classId;

    // (Constructor cho Mock (sửa lại))
    public Grade(int gradeId, String gradeType, Double score) { // Sửa kiểu
        this.gradeId = gradeId;
        this.score = score;
        this.gradeType = gradeType;
    }

    // Constructor đầy đủ (sửa lại)
    public Grade(int gradeId, String gradeType, Double score, String modifiedAt, Long studentId, Long classId) { // Sửa kiểu
        this.gradeId = gradeId;
        this.gradeType = gradeType;
        this.score = score;
        this.modifiedAt = modifiedAt;
        this.studentId = studentId;
        this.classId = classId;
    }

    public Grade() {
    }

    // --- Getters và Setters ---

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    // ⭐️ SỬA GETTER/SETTER CHO score
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
package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
// ⭐️ XÓA: import java.util.Date;

public class Grade {

    @SerializedName("gradeId")
    private int gradeId;

    @SerializedName("gradeType")
    private String gradeType;

    @SerializedName("score")
    private String score;

    // ⭐️ SỬA LẠI:
    // Khớp với JSON ("modifiedAt" là một String)
    @SerializedName("modifiedAt")
    private String modifiedAt;

    // ⭐️ THÊM VÀO:
    // Khớp với JSON (studentId là một số, có thể null)
    @SerializedName("studentId")
    private Integer studentId; // Dùng Integer (object) để chấp nhận null

    // ⭐️ THÊM VÀO:
    // Khớp với JSON (classId là một số, có thể null)
    @SerializedName("classId")
    private Integer classId;

    // (Constructor cho Mock (giữ nguyên))
    public Grade(int gradeId, String gradeType, String score) {
        this.gradeId = gradeId;
        this.score = score;
        this.gradeType = gradeType;
    }

    public Grade(int gradeId, String gradeType, String score, String modifiedAt, Integer studentId, Integer classId) {
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    // ⭐️ THÊM GETTERS/SETTERS CHO CÁC TRƯỜNG MỚI
    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }
}
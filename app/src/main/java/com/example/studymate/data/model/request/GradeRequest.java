package com.example.studymate.data.model.request;

import com.google.gson.annotations.SerializedName;

public class GradeRequest {

    @SerializedName("studentId")
    private Long studentId;

    @SerializedName("classId")
    private Integer classId;

    @SerializedName("gradeType")
    private String gradeType;

    @SerializedName("score")
    private Double score;

    public GradeRequest(Long studentId, Integer classId, String gradeType, Double score) {
        this.studentId = studentId;
        this.classId = classId;
        this.gradeType = gradeType;
        this.score = score;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
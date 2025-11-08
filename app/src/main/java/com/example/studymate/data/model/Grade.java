package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

import java.lang.Class;
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

}
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

}
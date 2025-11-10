package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class StudentClass {

    @SerializedName("studentClassId")
    private int studentClassId;

    @SerializedName("student")
    private User student;

    @SerializedName("class")
    private StudyClass stydyClass;

    @SerializedName("joinedAt")
    private Date joinedAt;

    @SerializedName("leftAt")
    private String leftAt;

}
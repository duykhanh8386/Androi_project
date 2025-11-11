package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Feedback {

    @SerializedName("feedbackId")
    private int feedbackId;

    @SerializedName("feedbackContent")
    private String feedbackContent;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("isRead")
    private String isRead;

    @SerializedName("class")
    private StudyClass studyClass;

    @SerializedName("sender")
    private User sender;

}
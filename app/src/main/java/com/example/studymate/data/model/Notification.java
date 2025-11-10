package com.example.studymate.data.model;

import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("notificationId")
    private int notificationId;

    @SerializedName("notificationTitle")
    private String notificationTitle;

    @SerializedName("notificationContent")
    private String notificationContent;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("class")
    private StudyClass studyClass;

    @SerializedName("teacher")
    private User teacher;

}
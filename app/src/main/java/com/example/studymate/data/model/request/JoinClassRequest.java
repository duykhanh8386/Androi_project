package com.example.studymate.data.model.request;

import com.google.gson.annotations.SerializedName;

public class JoinClassRequest {

    @SerializedName("classJoinCode")
    private String classJoinCode;

    public JoinClassRequest(String classJoinCode) {
        this.classJoinCode = classJoinCode;
    }
}
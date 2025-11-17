package com.example.studymate.data.model.request;

import com.google.gson.annotations.SerializedName;

public class UpdateClassRequest {

    @SerializedName("className")
    private String className;

    @SerializedName("classTime")
    private String classTime;

    public UpdateClassRequest(String className, String classTime) {
        this.className = className;
        this.classTime = classTime;
    }

}
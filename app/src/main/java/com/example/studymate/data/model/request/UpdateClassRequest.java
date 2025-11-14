package com.example.studymate.data.model.request;

import com.google.gson.annotations.SerializedName;

public class UpdateClassRequest {

    @SerializedName("className")
    private String className;

    @SerializedName("classTime")
    private String classTime;

    // (Backend của bạn có thể cần thêm "teacherId",
    // nhưng nếu dùng Auth (Cookie/Token) thì không cần)

    public UpdateClassRequest(String className, String classTime) {
        this.className = className;
        this.classTime = classTime;
    }

    // (Getters/Setters nếu cần)
}
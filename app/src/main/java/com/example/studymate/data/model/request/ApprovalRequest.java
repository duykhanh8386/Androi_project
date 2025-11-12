package com.example.studymate.data.model.request;

import com.google.gson.annotations.SerializedName;

public class ApprovalRequest {

    @SerializedName("status")
    private String status;

    public ApprovalRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
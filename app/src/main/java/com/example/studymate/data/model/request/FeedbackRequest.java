package com.example.studymate.data.model.request;

import com.google.gson.annotations.SerializedName;

public class FeedbackRequest {

    @SerializedName("classId")
    private int classId;

    @SerializedName("feedbackContent")
    private String feedbackContent;

    public FeedbackRequest(int classId, String feedbackContent) {
        this.classId = classId;
        this.feedbackContent = feedbackContent;
    }

    // Getters (nếu cần)
}

package com.example.studymate.data.model.response;

import com.google.gson.annotations.SerializedName;

public class MessageResponse {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    // Constructor cho mock data
    public MessageResponse(String message) {
        this.message = message;
    }
}
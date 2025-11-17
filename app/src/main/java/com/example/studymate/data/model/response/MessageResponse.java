package com.example.studymate.data.model.response;

import com.google.gson.annotations.SerializedName;

public class MessageResponse {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public MessageResponse(String message) {
        this.message = message;
    }
}
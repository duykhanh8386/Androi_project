package com.example.studymate.data.model.response;

import com.example.studymate.data.model.User;

public class LoginResponse {

    private String token;
    private User user;

    // Getters
    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}

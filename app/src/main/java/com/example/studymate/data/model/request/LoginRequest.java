package com.example.studymate.data.model.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    private String username;
    private String password;

    @SerializedName("role")
    private String role;

    public LoginRequest(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}


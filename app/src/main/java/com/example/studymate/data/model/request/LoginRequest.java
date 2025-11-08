package com.example.studymate.data.model.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    // Giữ tên biến Java là "username"
    private String username;
    private String password;

    // Dùng @SerializedName nếu tên key JSON khác tên biến Java
    // (Giả sử key JSON là "role")
    @SerializedName("role")
    private String role;

    // Cập nhật constructor
    public LoginRequest(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
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

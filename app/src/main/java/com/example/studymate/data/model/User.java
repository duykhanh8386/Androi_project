package com.example.studymate.data.model; // Hoặc package model của bạn

import com.google.gson.annotations.SerializedName;


public class User {

    @SerializedName("userId")
    private int userId;

    @SerializedName("fullname")
    private String fullName;

    @SerializedName("username")
    private String userName;

    // Password thường không được trả về trong API response,
    // nhưng nếu có thì đây là cách ánh xạ:
    @SerializedName("password")
    private String password;

    @SerializedName("status")
    private String status;

    @SerializedName("role_name")
    private String roleName;

    // Constructor rỗng (bắt buộc cho Gson)
    public User() {
    }

    @SerializedName("email") // Giả sử API/mock data có trường này
    private String email;

    public User(int userId, String fullName, String email, String roleName) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email; // Thêm vào
        this.roleName = roleName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
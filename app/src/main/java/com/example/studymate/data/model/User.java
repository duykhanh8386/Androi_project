package com.example.studymate.data.model; // Hoặc package model của bạn

import com.google.gson.annotations.SerializedName;


public class User {

    @SerializedName("userId")
    private int userId;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("username")
    private String username;

    // Password thường không được trả về trong API response,
    // nhưng nếu có thì đây là cách ánh xạ:
    @SerializedName("password")
    private String password;

    @SerializedName("enable")
    private Boolean enable;

    @SerializedName("roleName")
    private String roleName;

    // Constructor rỗng (bắt buộc cho Gson)
    public User() {
    }

    @SerializedName("email") // Giả sử API/mock data có trường này
    private String email;

    public User(int userId, String fullName, String username, String email, String roleName) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
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
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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
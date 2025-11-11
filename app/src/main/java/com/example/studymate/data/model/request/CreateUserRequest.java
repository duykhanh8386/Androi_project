package com.example.studymate.data.model.request;

public class CreateUserRequest {
    public String fullname;
    public String email;
    public String username;
    public String phone;
    public String password; // server sẽ hash trước khi lưu DB
    public String role; // TEACHER | STUDENT
    public String status; // ACTIVE | INACTIVE
}


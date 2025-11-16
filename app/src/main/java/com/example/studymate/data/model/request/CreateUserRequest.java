package com.example.studymate.data.model.request;

public class CreateUserRequest {
    public String fullName;

    public String username;

    public String password; // server sẽ hash trước khi lưu DB
    public String roleName; // TEACHER | STUDENT
    public boolean enable; // ACTIVE | INACTIVE
}


package com.example.studymate.data.model.response;

import com.example.studymate.data.model.Grade;
import com.example.studymate.data.model.User;

import java.util.List;

public class StudentResponse {
    private User user;
    private List<Grade> grades;

    public StudentResponse() {
    }

    public StudentResponse(User user, List<Grade> grades) {
        this.user = user;
        this.grades = grades;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}

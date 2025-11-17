package com.example.studymate.data.model.request;

public class UpdateStatusRequest {
    public String status; // INACTIVE | ACTIVE

    public UpdateStatusRequest(String status) {
        this.status = status;
    }
}

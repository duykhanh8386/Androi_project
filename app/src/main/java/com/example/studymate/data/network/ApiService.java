package com.example.studymate.data.network;


import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.request.JoinClassRequest;
import com.example.studymate.data.model.request.LoginRequest;
import com.example.studymate.data.model.response.LoginResponse;
import com.example.studymate.data.model.response.MessageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/auth/logout")
    Call<Void> logout();

    @GET("api/student/classes")
    Call<List<StudyClass>> getStudentClasses();

    @GET("api/student/classes/{id}")
    Call<StudyClass> getClassDetails(@Path("id") int classId);

    @POST("api/student/classes/join")
    Call<MessageResponse> joinClass(@Body JoinClassRequest joinRequest);

}

package com.example.studymate.data.network;


import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.User;
import com.example.studymate.data.model.request.CreateUserRequest;
import com.example.studymate.data.model.request.JoinClassRequest;
import com.example.studymate.data.model.request.LoginRequest;
import com.example.studymate.data.model.request.UpdateStatusRequest;
import com.example.studymate.data.model.response.LoginResponse;
import com.example.studymate.data.model.response.MessageResponse;


import java.util.List;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    // ===== AUTH =====
    @POST("api/auth/signin")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);


    @POST("api/auth/logout")
    Call<Void> logout();


    // ===== STUDENT =====
    @GET("api/student/classes")
    Call<List<StudyClass>> getStudentClasses();

    @GET("api/student/classes/{id}")
    Call<StudyClass> getClassDetails(@Path("id") int classId);

    // ===== ADMIN: USER MANAGEMENT =====
    @POST("api/admin/users")
    Call<User> createUser(@Body CreateUserRequest body);


    @GET("api/admin/users/search")
    Call<List<User>> searchUsers(
        @Query("keyword") String keyword,
        @Query("role") String role,
        @Query("status") String status,
        @Query("page") int page,
        @Query("size") int size
    );

    @POST("api/student/classes/join")
    Call<MessageResponse> joinClass(@Body JoinClassRequest joinRequest);

    @PATCH("api/admin/users/{id}/status")
    Call<Void> updateUserStatus(@Path("id") int userId, @Body UpdateStatusRequest body);
}

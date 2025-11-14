package com.example.studymate.data.network;


import com.example.studymate.data.model.Feedback;
import com.example.studymate.data.model.Grade;
import com.example.studymate.data.model.Notification;
import com.example.studymate.data.model.StudentClass;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.User;
import com.example.studymate.data.model.request.GradeRequest;
import com.example.studymate.data.model.request.UpdateClassRequest;
import com.example.studymate.data.model.request.CreateUserRequest;
import com.example.studymate.data.model.request.FeedbackRequest;
import com.example.studymate.data.model.request.JoinClassRequest;
import com.example.studymate.data.model.request.LoginRequest;
import com.example.studymate.data.model.request.UpdateStatusRequest;
import com.example.studymate.data.model.response.ClassDetailResponse;
import com.example.studymate.data.model.response.LoginResponse;
import com.example.studymate.data.model.response.MessageResponse;
import com.example.studymate.data.model.response.StudentResponse;


import java.util.List;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    // ===== AUTH =====
    @POST("api/auth/signin")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/auth/logout")
    Call<Void> logout();

    // ===== TEACHER =====

    @GET("api/teacher/classes/{id}/pending")
    Call<List<StudentClass>> getPendingStudents(@Path("id") int classId);

    @PUT("api/teacher/classes/students/{studentClassId}")
    Call<MessageResponse> approveOrRejectStudent(
            @Path("studentClassId") int studentClassId,
            @Query("status") String status
    );

    @PUT("api/teacher/classes/{classId}/students/{studentId}")
    Call<MessageResponse> kickStudent(
            @Path("classId") int classId,
            @Path("studentId") int studentId,
            @Query("status") String status
    );

    @PUT("api/teacher/classes/{id}/update-all")
    Call<MessageResponse> updateAllPendingStatus(
            @Path("id") int classId,
            @Query("status") String status // (APPROVED hoặc REJECTED)
    );

    @GET("api/teacher/classes")
    Call<List<StudyClass>> getTeacherClasses();

    @POST("api/teacher/classes")
    Call<StudyClass> createClass(@Body UpdateClassRequest createRequest);

    @PUT("api/teacher/classes/{classId}")
    Call<StudyClass> updateClass(
            @Path("classId") int classId,
            @Body UpdateClassRequest updateRequest
    );

    @DELETE("api/teacher/classes/{classId}")
    Call<MessageResponse> deleteClass(@Path("classId") int classId);

    @POST("api/teacher/grades")
    Call<Grade> addGrade(@Body GradeRequest gradeRequest);

    @PUT("api/teacher/grades/{gradeId}")
    Call<Grade> updateGrade(
            @Path("gradeId") int gradeId,
            @Body GradeRequest gradeRequest
    );

    @DELETE("api/teacher/grades/{gradeId}")
    Call<MessageResponse> deleteGrade(@Path("gradeId") int gradeId);

    // ===== STUDENT =====
    @GET("api/student/classes")
    Call<List<StudyClass>> getStudentClasses();

    @GET("api/user/classes/{id}")
    Call<ClassDetailResponse> getClassDetails(@Path("id") int classId);

    @GET("api/user/classes/{id}/students")
    Call<List<StudentResponse>> getStudentsInClass(@Path("id") int classId);

    @GET("api/user/classes/{id}/notifications")
    Call<List<Notification>> getNotifications(@Path("id") int classId);

    @GET("api/user/classes/notifications/{id}")
    Call<Notification> getNotificationDetail(@Path("id") int notificationId);

    @GET("api/student/classes/{classId}/grades")
    Call<List<Grade>> getStudentGrades(@Path("classId") int classId);

    @DELETE("api/student/classes/{id}")
    Call<MessageResponse> leaveClass(@Path("id") int classId);

    // ===== FEEDBACK ======
    @GET("api/student/feedback/{id}/conversations")
    Call<List<Feedback>> getFeedbackThread(@Path("id") int classId);

    @POST("api/student/feedback/send")
    Call<Feedback> sendFeedback(@Body FeedbackRequest feedbackRequest);

    // ===== ADMIN: USER MANAGEMENT =====
    @POST("api/admin/users")
    Call<User> createUser(@Body CreateUserRequest body);

    @GET("api/admin/users")
    Call<List<User>> searchUsers(
        @Query("keyword") String keyword,
        @Query("role") String role,
        @Query("status") String status
    );

    @POST("api/student/classes/join")
    Call<MessageResponse> joinClass(@Body JoinClassRequest joinRequest);

    @PATCH("api/admin/users/{id}/status")
    Call<Void> updateUserStatus(@Path("id") int userId, @Body UpdateStatusRequest body);
}

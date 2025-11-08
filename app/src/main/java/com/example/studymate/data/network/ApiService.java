package com.example.studymate.data.network;


import com.example.studymate.data.model.request.LoginRequest;
import com.example.studymate.data.model.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    /**
     * [cite_start]Dựa trên Bảng 2.5.1 (Use case Đăng nhập) [cite: 253, 296]
     * @param loginRequest Đối tượng Java (POJO) chứa username và password.
     * @return Một đối tượng Call chứa LoginResponse (ví dụ: token, thông tin user).
     */
    @POST("api/auth/login") // ⚠️ Đổi đường dẫn này cho khớp với Spring Boot
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    /**
     * [cite_start]Ví dụ cho Use case Tạo tài khoản (Bảng 2.5.3) [cite: 256, 302]
     * (Bạn sẽ cần tạo các lớp RegisterRequest/RegisterResponse trong data.model)
     */
    // @POST("api/auth/register")
    // Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    /**
     * [cite_start]Ví dụ cho Use case Quản lý lớp học (Bảng 2.5.5) [cite: 265, 308]
     * Lấy danh sách các lớp học của một giáo viên
     * (Giả sử TeacherID được lấy từ token, hoặc bạn có thể truyền nó vào)
     */
    // @GET("api/classes")
    // Call<List<Class>> getClasses();

    /**
     * [cite_start]Ví dụ cho Use case Tham gia lớp học (Bảng 2.5.11) [cite: 283, 326]
     * (Giả sử mã lớp học được gửi trong body của một JoinClassRequest)
     */
    // @POST("api/classes/join")
    // Call<Void> joinClass(@Body JoinClassRequest joinRequest);

}

package com.example.studymate.data.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.User;
import com.example.studymate.data.model.request.LoginRequest;
import com.example.studymate.data.model.response.LoginResponse;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private ApiService apiService;

    // Cải tiến: Dùng MutableLiveData làm thành viên (member)
    // để quản lý trạng thái.
    private MutableLiveData<LoginResponse> loginResponseData = new MutableLiveData<>();
    private MutableLiveData<String> loginErrorData = new MutableLiveData<>();

    private final boolean IS_MOCK_MODE = true;

    public AuthRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    /**
     * Hàm gọi API đăng nhập.
     * Giờ đây nó sẽ trả về void và cập nhật LiveData nội bộ.
     */
    public void login(String username, String password, String role) {
        // BƯỚC 1: KIỂM TRA CỜ NGAY TẠI ĐÂY
        if (IS_MOCK_MODE) {
            // Nếu là mock, gọi logic MẪU
            runMockLogic(username, password, role);
        } else {
            // Nếu là thật, gọi logic API THẬT
            runRealApiLogic(username, password, role);
        }
    }

    /**
     * Logic gọi API thật (code cũ của bạn)
     */
    private void runRealApiLogic(String username, String password, String role) {
        // Tạo đối tượng Request với 3 tham số
        LoginRequest loginRequest = new LoginRequest(username, password, role);

        // Gọi API thật
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    loginResponseData.postValue(response.body());
                } else {
                    loginErrorData.postValue("Lỗi đăng nhập: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginErrorData.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    /**
     * Logic trả về DỮ LIỆU MẪU
     */
    private void runMockLogic(String username, String password, String role) {
        // Giả lập độ trễ mạng (2 giây) để bạn có thể thấy ProgressBar
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kịch bản 1: Đăng nhập HỌC SINH thành công
                if (username.equals("student") && role.equals("STUDENT")) {

                    // Tạo một User mẫu
                    User mockUser = new User(1, "Học Sinh A", "student@test.com", "STUDENT");
                    // Tạo một Response mẫu
                    LoginResponse mockResponse = new LoginResponse("mock_token_student_123", mockUser);

                    loginResponseData.postValue(mockResponse);

                    // Kịch bản 2: Đăng nhập GIÁO VIÊN thành công
                } else if (username.equals("teacher") && role.equals("TEACHER")) {

                    User mockUser = new User(10, "Giáo Viên B", "teacher@test.com", "TEACHER");
                    LoginResponse mockResponse = new LoginResponse("mock_token_teacher_456", mockUser);

                    loginResponseData.postValue(mockResponse);

                    // Kịch bản 3: Đăng nhập ADMIN thành công
                } else if (username.equals("admin") && role.equals("ADMIN")) {

                    User mockUser = new User(100, "Quản Trị Viên", "admin@test.com", "ADMIN");
                    LoginResponse mockResponse = new LoginResponse("mock_token_admin_789", mockUser);

                    loginResponseData.postValue(mockResponse);

                    // Kịch bản 4: Đăng nhập thất bại (sai thông tin)
                } else {
                    loginErrorData.postValue("Lỗi: Sai tên đăng nhập hoặc mật khẩu (dữ liệu mẫu).");
                }
            }
        }, 2000); // Trì hoãn 2000ms = 2 giây
    }
    // --- Getters ---
    // ViewModel sẽ "lấy" các LiveData này để quan sát
    public LiveData<LoginResponse> getLoginResponseData() {
        return loginResponseData;
    }

    public LiveData<String> getLoginErrorData() {
        return loginErrorData;
    }
}
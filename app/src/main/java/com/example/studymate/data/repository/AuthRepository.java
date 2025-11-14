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
import com.example.studymate.data.network.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private ApiService apiService;
    private SessionManager sessionManager;

    private MutableLiveData<Boolean> logoutSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<LoginResponse> loginResponseData = new MutableLiveData<>();
    private MutableLiveData<String> loginErrorData = new MutableLiveData<>();

    private final boolean IS_MOCK_MODE = true;

    public AuthRepository() {
        this.apiService = RetrofitClient.getApiService();
        this.sessionManager = new SessionManager();
    }

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

    // ⭐️ THÊM HÀM NÀY:
    public void logout() {

        clearLocalUserData(); // Xóa token

        // --- BƯỚC 2: GỌI API ĐỂ VÔ HIỆU HÓA TOKEN (NẾU CẦN) ---
        // (Trong chế độ MOCK, chúng ta chỉ cần giả lập thành công)
        if (IS_MOCK_MODE) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                logoutSuccessEvent.postValue(true);
            }, 500); // Giả lập độ trễ 0.5 giây
        } else {
            // Logic gọi API thật
            apiService.logout().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    // Dù API thành công hay thất bại, phía client vẫn đăng xuất
                    logoutSuccessEvent.postValue(true);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Kể cả lỗi mạng, vẫn đăng xuất ở client
                    logoutSuccessEvent.postValue(true);
                }
            });
        }
    }

    private void clearLocalUserData() {
        sessionManager.clearAuthToken();
        System.out.println("Đã xóa dữ liệu token/user local.");
    }

    private void runRealApiLogic(String username, String password, String role) {
        // Tạo đối tượng Request với 3 tham số
        LoginRequest loginRequest = new LoginRequest(username, password, role);

        // Gọi API thật
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sessionManager.saveAuthToken(response.body().getToken());
                    sessionManager.saveUserId((long) response.body().getUser().getUserId());
                    sessionManager.saveUserRole(response.body().getUser().getRoleName());
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
                if (username.equals("student") && role.equals("ROLE_STUDENT")) {

                    // Tạo một User mẫu
                    User mockUser = new User(1, "Học Sinh A", "student", "student@test.com", "ROLE_STUDENT");
                    // Tạo một Response mẫu
                    LoginResponse mockResponse = new LoginResponse("mock_token_student_123", mockUser);
                    sessionManager.saveUserId(1L);
                    sessionManager.saveUserRole("ROLE_STUDENT");
                    loginResponseData.postValue(mockResponse);

                    // Kịch bản 2: Đăng nhập GIÁO VIÊN thành công
                } else if (username.equals("teacher") && role.equals("ROLE_TEACHER")) {

                    User mockUser = new User(10, "Giáo Viên B", "teacher" , "teacher@test.com", "ROLE_TEACHER");
                    LoginResponse mockResponse = new LoginResponse("mock_token_teacher_456", mockUser);
                    sessionManager.saveUserId(10L);
                    sessionManager.saveUserRole("ROLE_TEACHER");
                    loginResponseData.postValue(mockResponse);

                    // Kịch bản 3: Đăng nhập ADMIN thành công
                } else if (username.equals("admin") && role.equals("ROLE_ADMIN")) {

                    User mockUser = new User(100, "Quản Trị Viên", "admin", "admin@test.com", "ROLE_ADMIN");
                    LoginResponse mockResponse = new LoginResponse("mock_token_admin_789", mockUser);
                    sessionManager.saveUserId(100L);
                    sessionManager.saveUserRole("ROLE_ADMIN");
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

    public LiveData<Boolean> getLogoutSuccessEvent() {
        return logoutSuccessEvent;
    }
}
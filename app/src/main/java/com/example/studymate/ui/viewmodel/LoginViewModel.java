package com.example.studymate.ui.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studymate.data.model.response.LoginResponse;
import com.example.studymate.data.repository.AuthRepository;

/**
 * ViewModel cho LoginActivity.
 * Chịu trách nhiệm gọi Repository và cung cấp LiveData cho View.
 */
public class LoginViewModel extends ViewModel {

    private AuthRepository authRepository;

    // LiveData mà View sẽ quan sát
    private LiveData<LoginResponse> loginResponseLiveData;
    private LiveData<String> loginErrorLiveData;

    public LoginViewModel() {
        // Khởi tạo Repository
        this.authRepository = new AuthRepository();

        // Lấy LiveData từ Repository
        // ViewModel sẽ "bê" LiveData từ Repository và "trưng" ra cho View
        this.loginResponseLiveData = authRepository.getLoginResponseData();
        this.loginErrorLiveData = authRepository.getLoginErrorData();
    }

    /**
     * Hàm này được View (Activity) gọi khi người dùng nhấn nút Đăng nhập.
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     */
    public void performLogin(String username, String password, String role) {
        // Truyền "role" xuống cho Repository
        authRepository.login(username, password, role);
    }

    // --- Getters ---
    // View (Activity) sẽ dùng các hàm này để lấy LiveData và "quan sát"
    public LiveData<LoginResponse> getLoginResponse() {
        return loginResponseLiveData;
    }

    public LiveData<String> getLoginError() {
        return loginErrorLiveData;
    }
}
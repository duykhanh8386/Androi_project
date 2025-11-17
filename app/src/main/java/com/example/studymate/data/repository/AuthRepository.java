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

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private ApiService apiService;
    private SessionManager sessionManager;

    private MutableLiveData<Boolean> logoutSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<LoginResponse> loginResponseData = new MutableLiveData<>();
    private MutableLiveData<String> loginErrorData = new MutableLiveData<>();

    public AuthRepository() {
        this.apiService = RetrofitClient.getApiService();
        this.sessionManager = new SessionManager();
    }

    public void login(String username, String password, String role) {
        runRealApiLogic(username, password, role);
    }

    public void logout() {

        clearLocalUserData();
        RetrofitClient.clearCookies();
        apiService.logout().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                logoutSuccessEvent.postValue(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                logoutSuccessEvent.postValue(true);
            }
        });
    }

    private void clearLocalUserData() {
        sessionManager.clearUserData();
        System.out.println("Đã xóa dữ liệu token/user local.");
    }

    private void runRealApiLogic(String username, String password, String role) {
        LoginRequest loginRequest = new LoginRequest(username, password, role);
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sessionManager.saveAuthToken(response.body().getToken());
                    sessionManager.saveUserId((long) response.body().getUser().getUserId());
                    sessionManager.saveUserRole(response.body().getUser().getRoleName());
                    loginResponseData.postValue(response.body());
                } else {
                    try {
                        loginErrorData.postValue(response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginErrorData.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }
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
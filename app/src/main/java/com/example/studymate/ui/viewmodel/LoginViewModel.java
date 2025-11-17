package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studymate.data.model.response.LoginResponse;
import com.example.studymate.data.repository.AuthRepository;

public class LoginViewModel extends ViewModel {

    private AuthRepository authRepository;

    private LiveData<LoginResponse> loginResponseLiveData;
    private LiveData<String> loginErrorLiveData;

    public LoginViewModel() {
        this.authRepository = new AuthRepository();
        this.loginResponseLiveData = authRepository.getLoginResponseData();
        this.loginErrorLiveData = authRepository.getLoginErrorData();
    }

    public void performLogin(String username, String password, String role) {
        authRepository.login(username, password, role);
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return loginResponseLiveData;
    }

    public LiveData<String> getLoginError() {
        return loginErrorLiveData;
    }
}
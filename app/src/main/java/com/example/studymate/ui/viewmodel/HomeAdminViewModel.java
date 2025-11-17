package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studymate.data.repository.AuthRepository;

public class HomeAdminViewModel extends ViewModel {
    private AuthRepository authRepository;
    private LiveData<Boolean> logoutSuccessEvent;

    public HomeAdminViewModel() {
        this.authRepository = new AuthRepository();
        this.logoutSuccessEvent = authRepository.getLogoutSuccessEvent();
    }

    public void performLogout() {
        authRepository.logout();
    }

    public LiveData<Boolean> getLogoutSuccessEvent() {
        return logoutSuccessEvent;
    }

}

package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.repository.AuthRepository;
import com.example.studymate.data.repository.ClassRepository;

import java.util.List;


public class HomeAdminViewModel extends ViewModel {
    private AuthRepository authRepository;
    private LiveData<Boolean> logoutSuccessEvent;

    public HomeAdminViewModel() {
        // Khởi tạo Auth Repo
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

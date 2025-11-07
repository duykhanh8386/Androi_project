package com.example.studymate.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.repository.UserRepository;

import java.util.concurrent.Executors;

public class LoginViewModel extends AndroidViewModel {
    private final UserRepository repo;
    private final MutableLiveData<UserRepository.LoginState> loginState = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application app) {
        super(app);
        repo = new UserRepository(app.getApplicationContext());
    }

    /** Đăng nhập: truyền mật khẩu thô + role đã chọn */
    public void login(String username, String password, String role) {
        Executors.newSingleThreadExecutor().execute(() -> {
            UserRepository.LoginState st = repo.login(username, password, role);
            loginState.postValue(st);
        });
    }

    public LiveData<UserRepository.LoginState> getLoginState() { return loginState; }
}

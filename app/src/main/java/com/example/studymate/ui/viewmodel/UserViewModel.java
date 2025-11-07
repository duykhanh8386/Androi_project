package com.example.studymate.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.local.entity.User;
import com.example.studymate.repository.UserRepository;

import java.util.concurrent.Executors;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repo;
    private final MutableLiveData<UserRepository.LoginState> loginState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();

    public UserViewModel(@NonNull Application app) {
        super(app);
        repo = new UserRepository(app);
    }

    public LiveData<UserRepository.LoginState> getLoginState(){ return loginState; }
    public LiveData<Boolean> getLogoutResult(){ return logoutResult; }

    public void login(String username, String passwordHash, String role){
        Executors.newSingleThreadExecutor().execute(() -> {
            UserRepository.LoginState st = repo.login(username, passwordHash,role);
            loginState.postValue(st);
        });
    }

    public void logout(){
        Executors.newSingleThreadExecutor().execute(() -> logoutResult.postValue(repo.logout()));
    }

    public LiveData<User> observeUser(long id){ return repo.observe(id); }

    // Admin actions
    public LiveData<java.util.List<User>> searchAccounts(String keyword, String role, String status){
        return repo.search(keyword, role, status);
    }
    public MutableLiveData<Boolean> createResult = new MutableLiveData<>();
    public void createAccount(String fullname, String email, String username, String phone, String password, String role, boolean active){
        Executors.newSingleThreadExecutor().execute(() -> createResult.postValue(repo.createAccount(fullname,email,username,phone,password,role,active)));
    }
    public MutableLiveData<Boolean> disableResult = new MutableLiveData<>();
    public void disable(long userId){
        Executors.newSingleThreadExecutor().execute(() -> disableResult.postValue(repo.disable(userId)));
    }
}

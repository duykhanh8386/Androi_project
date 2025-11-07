package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studymate.data.local.entity.User;
import com.example.studymate.data.repository.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<User> getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public LiveData<List<User>> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void insert(User user) {
        userRepository.insert(user);
    }
}

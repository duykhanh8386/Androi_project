package com.example.studymate.data.repository;

import androidx.lifecycle.LiveData;

import com.example.studymate.data.local.dao.UserDao;
import com.example.studymate.data.local.entity.User;

import java.util.List;

public class UserRepository {
    private final UserDao userDao;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public void insert(User user) {
        new Thread(() -> userDao.insert(user)).start(); // Room không cho chạy trên main thread
    }

    public LiveData<User> getUserById(int id) {
        return userDao.getUserById(id);
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }
}

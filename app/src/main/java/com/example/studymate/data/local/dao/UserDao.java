package com.example.studymate.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.studymate.data.local.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE userID = :id")
    LiveData<User> getUserById(int id);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();
}

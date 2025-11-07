package com.example.studymate.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.studymate.data.local.entity.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    long insert(Notification n);

    @Query("SELECT * FROM notification WHERE classId = :classId ORDER BY createdAt DESC")
    LiveData<List<Notification>> listByClass(long classId);

    @Query("SELECT * FROM notification WHERE id = :id LIMIT 1")
    LiveData<Notification> observe(long id);
}

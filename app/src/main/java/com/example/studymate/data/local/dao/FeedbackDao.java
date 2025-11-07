package com.example.studymate.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.studymate.data.local.entity.Feedback;

import java.util.List;

@Dao
public interface FeedbackDao {
    @Insert
    long insert(Feedback f);

    @Query("SELECT * FROM feedback WHERE classId = :classId ORDER BY createdAt ASC")
    LiveData<List<Feedback>> observeByClass(long classId);

    @Query("UPDATE feedback SET isRead = 1 WHERE classId = :classId AND isRead = 0")
    void markAllRead(long classId);
}

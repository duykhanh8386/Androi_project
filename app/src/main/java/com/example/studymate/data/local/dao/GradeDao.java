package com.example.studymate.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.studymate.data.local.entity.Grade;

@Dao
public interface GradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(Grade g);

    @Query("SELECT * FROM grade WHERE classId = :classId AND studentId = :studentId LIMIT 1")
    LiveData<Grade> observe(long classId, long studentId);
}

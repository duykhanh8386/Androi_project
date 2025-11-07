package com.example.studymate.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studymate.data.local.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM `user` WHERE username = :username LIMIT 1")
    User findByUsername(String username);

    @Query("SELECT * FROM `user` WHERE id = :id")
    LiveData<User> observeUser(long id);

    @Insert
    long insert(User u);

    @Update
    void update(User u);

    // tên tham số khớp với tên trong query
    @Query("UPDATE `user` SET status = :status WHERE id = :userId")
    void updateStatus(long userId, String status);

    @Query("UPDATE `user` SET disabled = 1, status = 'INACTIVE' WHERE id = :userId")
    void disable(long userId);

    @Query("SELECT COUNT(*) FROM `user` WHERE username = :username")
    int countByUsername(String username);

    @Query(
        "SELECT * FROM `user` " +
            "WHERE (:keyword='' OR fullName LIKE '%'||:keyword||'%' " +
            "   OR username LIKE '%'||:keyword||'%' " +
            "   OR email LIKE '%'||:keyword||'%') " +
            "AND (:role='' OR role = :role) " +
            "AND (:status='' OR status = :status) " +
            "ORDER BY fullName ASC"
    )
    LiveData<List<User>> search(String keyword, String role, String status);
}

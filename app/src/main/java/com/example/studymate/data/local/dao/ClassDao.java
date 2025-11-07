package com.example.studymate.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.studymate.data.local.entity.ClassRoom;
import com.example.studymate.data.local.entity.StudentClass;

import java.util.List;

@Dao
public interface ClassDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertClass(ClassRoom c);

    @Query("SELECT * FROM class WHERE code = :code LIMIT 1")
    ClassRoom findByCode(String code);

    @Query("SELECT * FROM class ORDER BY name ASC")
    LiveData<List<ClassRoom>> allClasses();

    // student_class
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsertMembership(StudentClass sc);

    @Query("SELECT * FROM student_class WHERE classId = :classId AND studentId = :studentId LIMIT 1")
    StudentClass findMembership(long classId, long studentId);

    @Query("DELETE FROM student_class WHERE classId = :classId AND studentId = :studentId")
    void deleteMembership(long classId, long studentId);

    @Query("SELECT class.* FROM class INNER JOIN student_class ON class.id = student_class.classId WHERE student_class.studentId = :studentId AND student_class.status = 'MEMBER' ORDER BY class.name")
    LiveData<List<ClassRoom>> classesOfStudent(long studentId);

    @Query("SELECT * FROM class WHERE teacherId = :teacherId ORDER BY name")
    LiveData<List<ClassRoom>> classesOfTeacher(long teacherId);
}

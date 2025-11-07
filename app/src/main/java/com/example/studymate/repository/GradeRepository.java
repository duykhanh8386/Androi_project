package com.example.studymate.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.studymate.data.local.StudyMateDatabase;
import com.example.studymate.data.local.dao.GradeDao;
import com.example.studymate.data.local.entity.Grade;

public class GradeRepository {
    private final GradeDao dao;
    public GradeRepository(Context ctx){ dao = StudyMateDatabase.getInstance(ctx).gradeDao(); }

    public boolean save(long classId, long studentId, float quiz, float mid, float fin){
        try {
            Grade g = new Grade(); g.classId=classId; g.studentId=studentId; g.quiz=quiz; g.mid=mid; g.fin=fin;
            dao.upsert(g); return true;
        } catch (Exception e){ return false; }
    }

    public LiveData<Grade> observe(long classId, long studentId){ return dao.observe(classId, studentId); }
}

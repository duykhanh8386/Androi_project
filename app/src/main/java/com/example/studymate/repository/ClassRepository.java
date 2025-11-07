package com.example.studymate.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.studymate.data.local.StudyMateDatabase;
import com.example.studymate.data.local.dao.ClassDao;
import com.example.studymate.data.local.entity.ClassRoom;
import com.example.studymate.data.local.entity.StudentClass;

import java.util.List;

public class ClassRepository {
    private final ClassDao dao;
    public ClassRepository(Context ctx){ dao = StudyMateDatabase.getInstance(ctx).classDao(); }

    public LiveData<List<ClassRoom>> classesOfTeacher(long teacherId){ return dao.classesOfTeacher(teacherId); }
    public LiveData<List<ClassRoom>> classesOfStudent(long studentId){ return dao.classesOfStudent(studentId); }

    public static class JoinResult { public enum Code{ OK, EMPTY_CODE, INVALID_CODE, ALREADY_MEMBER, PENDING_EXISTS, DB_ERROR } public Code code; public JoinResult(Code c){ code=c; } }

    public JoinResult joinByCode(String code, long studentId){
        if (code == null || code.trim().isEmpty()) return new JoinResult(JoinResult.Code.EMPTY_CODE);
        try {
            ClassRoom c = dao.findByCode(code.trim());
            if (c == null) return new JoinResult(JoinResult.Code.INVALID_CODE);
            StudentClass sc = dao.findMembership(c.id, studentId);
            if (sc != null) {
                if ("MEMBER".equals(sc.status)) return new JoinResult(JoinResult.Code.ALREADY_MEMBER);
                if ("PENDING".equals(sc.status)) return new JoinResult(JoinResult.Code.PENDING_EXISTS);
            }
            StudentClass n = new StudentClass(); n.classId = c.id; n.studentId = studentId; n.status = "PENDING";
            dao.upsertMembership(n);
            return new JoinResult(JoinResult.Code.OK);
        } catch (Exception e){ return new JoinResult(JoinResult.Code.DB_ERROR); }
    }

    public boolean leaveClass(long classId, long userId) {
        // TODO: gọi DAO xóa khỏi bảng STUDENT_CLASS theo classId & userId
        return true;
    }
}

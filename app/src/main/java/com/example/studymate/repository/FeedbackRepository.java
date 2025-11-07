package com.example.studymate.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.studymate.data.local.StudyMateDatabase;
import com.example.studymate.data.local.dao.FeedbackDao;
import com.example.studymate.data.local.entity.Feedback;

import java.util.List;

public class FeedbackRepository {
    private final FeedbackDao dao;
    public FeedbackRepository(Context ctx){ dao = StudyMateDatabase.getInstance(ctx).feedbackDao(); }

    public LiveData<List<Feedback>> observe(long classId){ return dao.observeByClass(classId); }

    public boolean send(long classId, long senderId, String content, boolean teacherReply){
        try {
            Feedback f = new Feedback();
            f.classId = classId; f.senderId = senderId; f.message = content;
            f.createdAt = System.currentTimeMillis(); f.isRead = teacherReply; f.isTeacherReply = teacherReply;
            dao.insert(f);
            return true;
        } catch (Exception e){ return false; }
    }

    public void markRead(long classId){ try { dao.markAllRead(classId); } catch (Exception ignored){} }
}

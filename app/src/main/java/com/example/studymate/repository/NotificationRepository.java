package com.example.studymate.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.studymate.data.local.StudyMateDatabase;
import com.example.studymate.data.local.dao.NotificationDao;
import com.example.studymate.data.local.entity.Notification;

import java.util.List;

public class NotificationRepository {
    private final NotificationDao dao;
    public NotificationRepository(Context ctx){ dao = StudyMateDatabase.getInstance(ctx).notificationDao(); }

    public LiveData<List<Notification>> listByClass(long classId){ return dao.listByClass(classId); }

    public boolean create(long classId, long senderId, String title, String content){
        try {
            Notification n = new Notification();
            n.classId = classId; n.senderId = senderId; n.title = title; n.content = content;
            n.createdAt = System.currentTimeMillis();
            dao.insert(n); return true;
        } catch (Exception e){ return false; }
    }
}

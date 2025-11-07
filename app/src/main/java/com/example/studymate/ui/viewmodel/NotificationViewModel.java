package com.example.studymate.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.local.entity.Notification;
import com.example.studymate.repository.NotificationRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class NotificationViewModel extends AndroidViewModel {
    private final NotificationRepository repo;
    public NotificationViewModel(@NonNull Application app) {
        super(app); repo = new NotificationRepository(app);
    }

    public LiveData<List<Notification>> listByClass(long classId){ return repo.listByClass(classId); }

    public final MutableLiveData<Boolean> createResult = new MutableLiveData<>();
    public void create(long classId, long senderId, String title, String content){
        Executors.newSingleThreadExecutor().execute(() -> {
            if (title == null || title.trim().isEmpty() || title.length() > 200 || content == null || content.trim().isEmpty()) {
                createResult.postValue(null); return;
            }
            createResult.postValue(repo.create(classId, senderId, title.trim(), content.trim()));
        });
    }
}

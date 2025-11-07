package com.example.studymate.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.local.entity.Feedback;
import com.example.studymate.repository.FeedbackRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class FeedbackViewModel extends AndroidViewModel {
    private final FeedbackRepository repo;
    private LiveData<List<Feedback>> messages;
    public final MutableLiveData<Boolean> sendResult = new MutableLiveData<>();

    public FeedbackViewModel(@NonNull Application app) {
        super(app);
        repo = new FeedbackRepository(app);
    }

    public void load(long classId){ messages = repo.observe(classId); repo.markRead(classId); }
    public LiveData<List<Feedback>> getMessages(){ return messages; }

    public void send(long classId, long senderId, String content, boolean teacherReply){
        Executors.newSingleThreadExecutor().execute(() -> {
            if (content == null || content.trim().isEmpty()) { sendResult.postValue(null); return; }
            boolean ok = repo.send(classId, senderId, content.trim(), teacherReply);
            sendResult.postValue(ok);
        });
    }
}

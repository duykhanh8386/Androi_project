package com.example.studymate.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.Notification;
import com.example.studymate.data.repository.NotificationRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class NotificationViewModel extends AndroidViewModel {

    public NotificationViewModel(@NonNull Application application) {
        super(application);
    }
}

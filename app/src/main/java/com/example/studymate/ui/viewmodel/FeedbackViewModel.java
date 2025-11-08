package com.example.studymate.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.Feedback;
import com.example.studymate.data.repository.FeedbackRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class FeedbackViewModel extends AndroidViewModel {

    public FeedbackViewModel(@NonNull Application application) {
        super(application);
    }
}

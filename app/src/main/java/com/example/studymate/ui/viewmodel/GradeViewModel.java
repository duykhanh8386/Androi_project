package com.example.studymate.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.Grade;
import com.example.studymate.data.repository.GradeRepository;

import java.util.concurrent.Executors;

public class GradeViewModel extends AndroidViewModel {

    public GradeViewModel(@NonNull Application application) {
        super(application);
    }
}

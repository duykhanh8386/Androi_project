package com.example.studymate.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.local.entity.Grade;
import com.example.studymate.repository.GradeRepository;

import java.util.concurrent.Executors;

public class GradeViewModel extends AndroidViewModel {
    private final GradeRepository repo;
    public GradeViewModel(@NonNull Application app) { super(app); repo = new GradeRepository(app); }

    public LiveData<Grade> observe(long classId, long studentId){ return repo.observe(classId, studentId); }

    public final MutableLiveData<Boolean> saveResult = new MutableLiveData<>();
    public void save(long classId, long studentId, float quiz, float mid, float fin){
        Executors.newSingleThreadExecutor().execute(() -> saveResult.postValue(repo.save(classId, studentId, quiz, mid, fin)));
    }
}

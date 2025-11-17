package com.example.studymate.ui.viewmodel.teacher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.repository.TeacherRepository;

public class ClassCreateViewModel extends ViewModel {

    private TeacherRepository repository;

    public ClassCreateViewModel() {
        this.repository = new TeacherRepository();
    }

    public void performCreateClass(String className, String classTime) {
        repository.createClass(className, classTime);
    }

    public LiveData<StudyClass> getCreateSuccess() {
        return repository.getCreateClassSuccessEvent();
    }
    public LiveData<Boolean> getIsCreating() {
        return repository.getIsCreatingClass();
    }
    public LiveData<String> getCreateError() {
        return repository.getCreateClassErrorEvent();
    }
}
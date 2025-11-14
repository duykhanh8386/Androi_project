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

    // Fragment sẽ gọi hàm này
    public void performCreateClass(String className, String classTime) {
        repository.createClass(className, classTime);
    }

    // Getters để Fragment quan sát
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
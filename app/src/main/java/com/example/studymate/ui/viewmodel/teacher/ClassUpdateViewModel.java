package com.example.studymate.ui.viewmodel.teacher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.repository.TeacherRepository;

public class ClassUpdateViewModel extends ViewModel {

    private TeacherRepository repository;

    public ClassUpdateViewModel() {
        this.repository = new TeacherRepository();
    }

    // Fragment sẽ gọi hàm này
    public void performUpdateClass(int classId, String className, String classTime) {
        repository.updateClass(classId, className, classTime);
    }

    // Getters để Fragment quan sát
    public LiveData<StudyClass> getUpdateSuccess() {
        return repository.getUpdateClassSuccessEvent();
    }
    public LiveData<Boolean> getIsUpdating() {
        return repository.getIsUpdatingClass();
    }
    public LiveData<String> getUpdateError() {
        return repository.getUpdateClassErrorEvent();
    }
}
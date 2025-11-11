package com.example.studymate.ui.viewmodel.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.repository.ClassRepository;

public class JoinClassViewModel extends ViewModel {

    private ClassRepository classRepository;

    public JoinClassViewModel() {
        this.classRepository = new ClassRepository();
    }

    public void performJoinClass(String classCode) {
        classRepository.joinClass(classCode);
    }

    // Getters để Fragment quan sát
    public LiveData<String> getJoinSuccessEvent() {
        return classRepository.getJoinClassSuccess();
    }
    public LiveData<String> getJoinErrorEvent() {
        return classRepository.getJoinClassError();
    }
    public LiveData<Boolean> getIsLoading() {
        return classRepository.getIsJoinClassLoading();
    }
}

package com.example.studymate.ui.viewmodel.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.repository.StudentRepository;

public class JoinClassViewModel extends ViewModel {

    private StudentRepository studentRepository;

    public JoinClassViewModel() {
        this.studentRepository = new StudentRepository();
    }

    public void performJoinClass(String classCode) {
        studentRepository.joinClass(classCode);
    }

    public LiveData<String> getJoinSuccessEvent() {
        return studentRepository.getJoinClassSuccess();
    }
    public LiveData<String> getJoinErrorEvent() {
        return studentRepository.getJoinClassError();
    }
    public LiveData<Boolean> getIsLoading() {
        return studentRepository.getIsJoinClassLoading();
    }
}

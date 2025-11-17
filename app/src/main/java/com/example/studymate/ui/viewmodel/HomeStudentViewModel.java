package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.repository.AuthRepository;
import com.example.studymate.data.repository.ClassRepository;

import java.util.List;

public class HomeStudentViewModel extends ViewModel {
    private AuthRepository authRepository;
    private LiveData<Boolean> logoutSuccessEvent;

    private ClassRepository classRepository;
    private LiveData<List<StudyClass>> classListLiveData;
    private LiveData<Boolean> isLoading;

    public HomeStudentViewModel() {
        this.authRepository = new AuthRepository();
        this.logoutSuccessEvent = authRepository.getLogoutSuccessEvent();

        this.classRepository = new ClassRepository();
        this.classListLiveData = classRepository.getStudentClassListLiveData();
        this.isLoading = classRepository.getIsStudentClassListLoading();

    }

    public void fetchStudentClasses() {
        classRepository.fetchStudentClasses();
    }

    public LiveData<List<StudyClass>> getClassList() {
        return classListLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void performLogout() {
        authRepository.logout();
    }

    public LiveData<Boolean> getLogoutSuccessEvent() {
        return logoutSuccessEvent;
    }

}

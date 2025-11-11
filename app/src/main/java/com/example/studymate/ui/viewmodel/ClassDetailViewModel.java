package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.repository.ClassRepository;

public class ClassDetailViewModel extends ViewModel {

    private ClassRepository classRepository;
    private LiveData<StudyClass> classDetail;
    private LiveData<Boolean> isLoading;
    private LiveData<String> error;

    private LiveData<Boolean> isLeaveLoading;
    private LiveData<String> leaveSuccess;
    private LiveData<String> leaveError;

    public ClassDetailViewModel() {
        classRepository = new ClassRepository();
        classDetail = classRepository.getClassDetailLiveData();
        isLoading = classRepository.getIsDetailLoading();
        error = classRepository.getClassDetailError();

        isLeaveLoading = classRepository.getIsLeaveLoading();
        leaveSuccess = classRepository.getLeaveSuccessEvent();
        leaveError = classRepository.getLeaveErrorEvent();
    }

    // Hàm này được Fragment gọi
    public void loadClassDetails(int classId) {
        classRepository.fetchClassDetails(classId);
    }

    // Getters để Fragment quan sát
    public LiveData<StudyClass> getClassDetail() {
        return classDetail;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void performLeaveClass(int classId) {
        classRepository.leaveClass(classId);
    }
    public LiveData<Boolean> getIsLeaveLoading() { return isLeaveLoading; }
    public LiveData<String> getLeaveSuccess() { return leaveSuccess; }
    public LiveData<String> getLeaveError() { return leaveError; }
}

package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.response.ClassDetailResponse;
import com.example.studymate.data.repository.ClassRepository;

public class ClassDetailViewModel extends ViewModel {

    private ClassRepository classRepository;

    // LiveData "CHI TIÊT LỚP HỌC"
    private LiveData<ClassDetailResponse> classDetail;
    private LiveData<Boolean> isLoading;
    private LiveData<String> error;

    // LiveData "RỜI LỚP"
    private LiveData<Boolean> isLeaveLoading;
    private LiveData<String> leaveSuccess;
    private LiveData<String> leaveError;

    // LiveData "XÓA LỚP"
    private LiveData<Boolean> isDeletingClass;
    private LiveData<String> deleteSuccess;
    private LiveData<String> deleteError;

    public ClassDetailViewModel() {
        classRepository = new ClassRepository();
        classDetail = classRepository.getClassDetailLiveData();
        isLoading = classRepository.getIsDetailLoading();
        error = classRepository.getClassDetailError();

        isLeaveLoading = classRepository.getIsLeaveLoading();
        leaveSuccess = classRepository.getLeaveSuccessEvent();
        leaveError = classRepository.getLeaveErrorEvent();

        isDeletingClass = classRepository.getIsDeletingClass();
        deleteSuccess = classRepository.getDeleteSuccessEvent();
        deleteError = classRepository.getDeleteErrorEvent();
    }

    // Hàm này được Fragment gọi
    public void loadClassDetails(int classId) {
        classRepository.fetchClassDetails(classId);
    }

    // Getters để Fragment quan sát
    public LiveData<ClassDetailResponse> getClassDetail() {
        return classDetail;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void performLeaveClass(int classId) {
        classRepository.leaveClass(classId);
    }
    public LiveData<Boolean> getIsLeaveLoading() { return isLeaveLoading; }
    public LiveData<String> getLeaveSuccess() { return leaveSuccess; }
    public LiveData<String> getLeaveError() { return leaveError; }

    public void performDeleteClass(int classId) {
        classRepository.deleteClass(classId);
    }

    public LiveData<Boolean> getIsDeleting() { return isDeletingClass; }
    public LiveData<String> getDeleteSuccess() { return deleteSuccess; }
    public LiveData<String> getDeleteError() { return deleteError; }
}

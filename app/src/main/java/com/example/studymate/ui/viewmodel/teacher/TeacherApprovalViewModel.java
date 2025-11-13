package com.example.studymate.ui.viewmodel.teacher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studymate.data.model.StudentClass;
import com.example.studymate.data.model.User;
import com.example.studymate.data.repository.TeacherRepository;
import java.util.List;

public class TeacherApprovalViewModel extends ViewModel {

    private TeacherRepository repository;

    public TeacherApprovalViewModel() {
        this.repository = new TeacherRepository();
    }

    // --- (Lấy danh sách) ---
    public void loadPendingList(int classId) {
        repository.fetchPendingList(classId);
    }
    public LiveData<List<StudentClass>> getPendingList() {
        return repository.getPendingList();
    }
    public LiveData<Boolean> getIsLoading() {
        return repository.getIsLoading();
    }

    // --- (Xử lý 1 item) ---
    public void approveStudent(int studentClassId) {
        repository.approveStudent(studentClassId);
    }
    public void rejectStudent(int studentClassId) {
        repository.rejectStudent(studentClassId);
    }
    public LiveData<String> getApprovalSuccess() {
        return repository.getApprovalSuccessEvent();
    }
    public LiveData<String> getApprovalError() {
        return repository.getApprovalErrorEvent();
    }
    public LiveData<Boolean> getIsUpdating() {
        return repository.getIsUpdating();
    }

    // TODO: Thêm logic cho Approve All / Reject All
}
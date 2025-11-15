package com.example.studymate.ui.viewmodel.teacher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Grade;
import com.example.studymate.data.model.response.MessageResponse; // ⭐️ THÊM
import com.example.studymate.data.repository.TeacherRepository;

public class GradeEditViewModel extends ViewModel {

    private TeacherRepository repository;

    public GradeEditViewModel() {
        this.repository = new TeacherRepository();
    }

    // --- 1. LOGIC THÊM ĐIỂM (ĐÃ CÓ) ---
    public void performAddGrade(Long studentId, Integer classId, String gradeType, Double score) {
        repository.addGrade(studentId, classId, gradeType, score);
    }
    public LiveData<Grade> getAddSuccess() {
        return repository.getAddGradeSuccessEvent();
    }
    public LiveData<Boolean> getIsAdding() {
        return repository.getIsAddingGrade();
    }
    public LiveData<String> getAddError() {
        return repository.getAddGradeErrorEvent();
    }

    // ⭐️ 2. THÊM LOGIC SỬA ĐIỂM (MỚI) ⭐️
    public void performUpdateGrade(int gradeId, Long studentId, Integer classId, String gradeType, Double score) {
        repository.updateGrade(gradeId, studentId, classId, gradeType, score);
    }
    public LiveData<Grade> getUpdateSuccess() {
        return repository.getUpdateGradeSuccessEvent();
    }
    public LiveData<Boolean> getIsUpdating() {
        return repository.getIsUpdatingGrade();
    }
    public LiveData<String> getUpdateError() {
        return repository.getUpdateGradeErrorEvent();
    }

    // ⭐️ 3. THÊM LOGIC XÓA ĐIỂM (MỚI) ⭐️
    public void performDeleteGrade(int gradeId) {
        repository.deleteGrade(gradeId);
    }
    public LiveData<MessageResponse> getDeleteSuccess() {
        return repository.getDeleteGradeSuccessEvent();
    }
    public LiveData<Boolean> getIsDeleting() {
        return repository.getIsDeletingGrade();
    }
    public LiveData<String> getDeleteError() {
        return repository.getDeleteGradeErrorEvent();
    }
}
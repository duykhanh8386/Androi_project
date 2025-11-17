package com.example.studymate.ui.viewmodel.teacher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.response.StudentResponse;
import com.example.studymate.data.repository.TeacherRepository;
import java.util.List;

public class StudentManageViewModel extends ViewModel {

    private TeacherRepository repository;

    public StudentManageViewModel() {
        this.repository = new TeacherRepository();
    }

    public void loadStudentList(int classId) {
        repository.fetchStudentList(classId);
    }
    public LiveData<List<StudentResponse>> getStudentList() {
        return repository.getStudentList();
    }
    public LiveData<Boolean> getIsLoading() {
        return repository.getIsStudentListLoading();
    }
    public LiveData<String> getError() {
        return repository.getStudentListError();
    }

    public void kickStudent(int studentId, int classId) {
        repository.rejectStudent(studentId, classId);
    }

    public LiveData<String> getKickSuccess() {
        return repository.getApprovalSuccessEvent();
    }
    public LiveData<String> getKickError() {
        return repository.getApprovalErrorEvent();
    }
    public LiveData<Boolean> getIsKicking() {
        return repository.getIsApprovalLoading();
    }
}
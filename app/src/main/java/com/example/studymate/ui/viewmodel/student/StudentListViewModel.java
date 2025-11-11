package com.example.studymate.ui.viewmodel.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.User;
import com.example.studymate.data.model.response.StudentResponse;
import com.example.studymate.data.repository.ClassRepository;
import com.example.studymate.data.repository.StudentRepository;

import java.util.List;

public class StudentListViewModel extends ViewModel {

    private StudentRepository studentRepository;

    public StudentListViewModel() {
        this.studentRepository = new StudentRepository();
    }

    // Fragment sẽ gọi hàm này
    public void loadStudentList(int classId) {
        studentRepository.fetchStudentList(classId);
    }

    // Getters để Fragment quan sát
    public LiveData<List<StudentResponse>> getStudentList() {
        return studentRepository.getStudentListLiveData();
    }
    public LiveData<Boolean> getIsLoading() {
        return studentRepository.getIsStudentListLoading();
    }
}

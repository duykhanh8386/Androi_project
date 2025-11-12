package com.example.studymate.ui.viewmodel.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Grade;
import com.example.studymate.data.repository.StudentRepository; // ⭐️ Dùng Repo mới
import java.util.List;

public class StudentGradeViewModel extends ViewModel {

    private StudentRepository studentRepository;

    public StudentGradeViewModel() {
        this.studentRepository = new StudentRepository(); // ⭐️
    }

    // Fragment sẽ gọi hàm này
    public void loadGrades(int classId) {
        studentRepository.fetchStudentGrades(classId); // ⭐️
    }

    // Getters để Fragment quan sát
    public LiveData<List<Grade>> getGradeList() {
        return studentRepository.getGradeList(); // ⭐️
    }
    public LiveData<Boolean> getIsLoading() {
        return studentRepository.getIsLoading(); // ⭐️
    }

    public LiveData<String> getError() {
        return studentRepository.getError();
    }
}

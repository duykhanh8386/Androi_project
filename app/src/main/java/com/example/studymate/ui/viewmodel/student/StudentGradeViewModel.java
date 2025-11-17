package com.example.studymate.ui.viewmodel.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Grade;
import com.example.studymate.data.repository.StudentRepository;
import java.util.List;

public class StudentGradeViewModel extends ViewModel {

    private StudentRepository studentRepository;

    public StudentGradeViewModel() {
        this.studentRepository = new StudentRepository();
    }

    public void loadGrades(int classId) {
        studentRepository.fetchStudentGrades(classId);
    }


    public LiveData<List<Grade>> getGradeList() {
        return studentRepository.getGradeList();
    }
    public LiveData<Boolean> getIsLoading() {
        return studentRepository.getIsLoading();
    }

    public LiveData<String> getError() {
        return studentRepository.getError();
    }
}

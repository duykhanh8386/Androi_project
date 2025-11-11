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

    public ClassDetailViewModel() {
        classRepository = new ClassRepository();
        classDetail = classRepository.getClassDetailLiveData();
        isLoading = classRepository.getIsDetailLoading();
        error = classRepository.getClassDetailError();
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
}

package com.example.studymate.ui.viewmodel.teacher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Feedback;
import com.example.studymate.data.repository.FeedbackRepository;
import java.util.List;

public class FeedbackListViewModel extends ViewModel {

    private FeedbackRepository repository;

    public FeedbackListViewModel() {
        // (Khởi tạo Repository)
        this.repository = new FeedbackRepository();
    }

    // Fragment sẽ gọi hàm này
    public void loadFeedbackList(int classId) {
        repository.fetchTeacherFeedbackList(classId);
    }

    // Getters để Fragment quan sát
    public LiveData<List<Feedback>> getFeedbackList() {
        return repository.getFeedbackList();
    }
    public LiveData<Boolean> getIsLoading() {
        return repository.getIsLoadingList();
    }
    public LiveData<String> getError() {
        return repository.getListError();
    }
}
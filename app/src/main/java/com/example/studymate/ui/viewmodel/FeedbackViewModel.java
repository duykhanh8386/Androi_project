package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Feedback;
import com.example.studymate.data.repository.FeedbackRepository; // ⭐️ Dùng Repo mới
import java.util.List;

public class FeedbackViewModel extends ViewModel {

    private FeedbackRepository feedbackRepository;

    public FeedbackViewModel() {
        this.feedbackRepository = new FeedbackRepository(); // ⭐️
    }

    // Fragment sẽ gọi hàm này
    public void loadFeedback(int classId) {
        feedbackRepository.fetchFeedbackThread(classId); // ⭐️
    }

    // Getters để Fragment quan sát
    public LiveData<List<Feedback>> getFeedbackList() {
        return feedbackRepository.getFeedbackList(); // ⭐️
    }
    public LiveData<Boolean> getIsLoading() {
        return feedbackRepository.getIsLoading(); // ⭐️
    }
}

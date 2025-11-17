package com.example.studymate.ui.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Feedback;
import com.example.studymate.data.model.request.FeedbackRequest;
import com.example.studymate.data.repository.FeedbackRepository;
import java.util.List;

public class FeedbackViewModel extends ViewModel {

    private FeedbackRepository feedbackRepository;

    public FeedbackViewModel() {
        this.feedbackRepository = new FeedbackRepository();
    }

    public void loadFeedbackThread(int classId, @Nullable Long studentId) {
        feedbackRepository.getFeedbackThread(classId, studentId);
    }

    public LiveData<List<Feedback>> getFeedbackThread() {
        return feedbackRepository.getFeedbackThread();
    }
    public LiveData<Boolean> getIsLoading() {
        return feedbackRepository.getIsLoadingThread();
    }

    public LiveData<String> getError() {
        return feedbackRepository.getThreadError();
    }

    public void sendFeedback(FeedbackRequest request) {
        feedbackRepository.sendFeedback(request);
    }
    public LiveData<Feedback> getSendSuccess() {
        return feedbackRepository.getSendSuccessEvent();
    }
    public LiveData<String> getSendError() {
        return feedbackRepository.getSendErrorEvent();
    }
}

package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Feedback;
import com.example.studymate.data.model.request.FeedbackRequest;
import com.example.studymate.data.repository.FeedbackRepository;

public class FeedbackResponseViewModel extends ViewModel {

    private FeedbackRepository repository;

    public FeedbackResponseViewModel() {
        this.repository = new FeedbackRepository();
    }

    public void sendFeedback(int classId, String content, Long receiverId) {
        FeedbackRequest request = new FeedbackRequest(classId, content, receiverId);
        repository.sendFeedback(request);
    }

    public LiveData<Feedback> getSendSuccess() {
        return repository.getSendSuccessEvent();
    }

    public LiveData<Boolean> getIsSending() {
        return repository.getSendLoading();
    }

    public LiveData<String> getSendError() {
        return repository.getSendErrorEvent();
    }
}
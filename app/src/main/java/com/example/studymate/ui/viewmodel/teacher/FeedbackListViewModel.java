package com.example.studymate.ui.viewmodel.teacher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Feedback;
import com.example.studymate.data.repository.FeedbackRepository;
import java.util.List;

public class FeedbackListViewModel extends ViewModel {

    private FeedbackRepository repository;

    public FeedbackListViewModel() {
        this.repository = new FeedbackRepository();
    }

    public void loadFeedbackList(int classId) {
        repository.fetchTeacherFeedbackList(classId);
    }

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
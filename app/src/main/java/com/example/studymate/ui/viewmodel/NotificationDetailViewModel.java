package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Notification;
import com.example.studymate.data.repository.NotificationRepository;

public class NotificationDetailViewModel extends ViewModel {

    private NotificationRepository repository;

    public NotificationDetailViewModel() {
        this.repository = new NotificationRepository();
    }

    // Fragment sẽ gọi hàm này
    public void loadNotificationDetail(int notificationId) {
        repository.fetchNotificationDetail(notificationId);
    }

    // Getters để Fragment quan sát
    public LiveData<Notification> getNotificationDetail() {
        return repository.getNotificationDetail();
    }
    public LiveData<Boolean> getIsLoading() {
        return repository.getIsDetailLoading();
    }
}
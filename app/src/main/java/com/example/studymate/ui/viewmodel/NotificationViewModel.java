package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Notification;
import com.example.studymate.data.repository.NotificationRepository;
import java.util.List;

public class NotificationViewModel extends ViewModel {

    private NotificationRepository notificationRepository;

    public NotificationViewModel() {
        this.notificationRepository = new NotificationRepository();
    }

    public void loadNotifications(int classId) {
        notificationRepository.fetchNotificationList(classId);
    }

    public LiveData<List<Notification>> getNotificationList() {
        return notificationRepository.getNotificationList();
    }
    public LiveData<Boolean> getIsLoading() {
        return notificationRepository.getIsNotificationListLoading();
    }
    public LiveData<String> getError() {
        return notificationRepository.getNotificationListError();
    }

    public void performCreateNotification(int classId, String title, String content) {
        notificationRepository.createNotification(classId, title, content);
    }
    public LiveData<Notification> getCreateSuccessEvent() {
        return notificationRepository.getCreateSuccessEvent();
    }
    public LiveData<Boolean> getIsCreating() {
        return notificationRepository.getIsCreating();
    }
    public LiveData<String> getCreateErrorEvent() {
        return notificationRepository.getCreateErrorEvent();
    }
}
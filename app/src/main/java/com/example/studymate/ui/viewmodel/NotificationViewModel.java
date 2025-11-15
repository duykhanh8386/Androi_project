package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Notification;
// ⭐️ SỬA IMPORT:
import com.example.studymate.data.repository.NotificationRepository;
import java.util.List;

public class NotificationViewModel extends ViewModel {

    // ⭐️ SỬA BIẾN NÀY:
    private NotificationRepository notificationRepository;

    public NotificationViewModel() {
        // ⭐️ SỬA LẠI:
        this.notificationRepository = new NotificationRepository();
    }

    // Fragment sẽ gọi hàm này
    public void loadNotifications(int classId) {
        // ⭐️ SỬA LẠI:
        notificationRepository.fetchNotificationList(classId);
    }

    // Getters để Fragment quan sát
    public LiveData<List<Notification>> getNotificationList() {
        // ⭐️ SỬA LẠI:
        return notificationRepository.getNotificationList();
    }
    public LiveData<Boolean> getIsLoading() {
        // ⭐️ SỬA LẠI:
        return notificationRepository.getIsNotificationListLoading();
    }
    public LiveData<String> getError() {
        // ⭐️ SỬA LẠI:
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
package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Notification;
// ⭐️ SỬA IMPORT:
import com.example.studymate.data.repository.NotificationRepository;
import java.util.List;

public class NotificationListViewModel extends ViewModel {

    // ⭐️ SỬA BIẾN NÀY:
    private NotificationRepository notificationRepository;

    public NotificationListViewModel() {
        // ⭐️ SỬA LẠI:
        this.notificationRepository = new NotificationRepository();
    }

    // Fragment sẽ gọi hàm này
    public void loadNotificationList(int classId) {
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
}
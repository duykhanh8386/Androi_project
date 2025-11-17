package com.example.studymate.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.Notification;
import com.example.studymate.data.model.request.NotificationRequest;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {

    private ApiService apiService;

    // LiveData cho danh sách thông báo
    private MutableLiveData<List<Notification>> notificationListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNotificationListLoading = new MutableLiveData<>();
    private MutableLiveData<String> notificationListError = new MutableLiveData<>();

    // LiveData cho CHI TIẾT
    private MutableLiveData<Notification> notificationDetailLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDetailLoading = new MutableLiveData<>();
    private MutableLiveData<String> detailError = new MutableLiveData<>();

    // LiveData cho sự kiện TẠO MỚI
    private MutableLiveData<Notification> createSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> createErrorEvent = new MutableLiveData<>();
    private MutableLiveData<Boolean> isCreating = new MutableLiveData<>();

    public NotificationRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public void fetchNotificationList(int classId) {
        isNotificationListLoading.postValue(true);
        runRealApiLogicForNotificationList(classId);
    }

    public void fetchNotificationDetail(int notificationId) {
        isDetailLoading.postValue(true);
        runRealApiLogicForDetail(notificationId);
    }

    public void createNotification(int classId, String title, String content) {
        isCreating.postValue(true);
        NotificationRequest request = new NotificationRequest(title, content);
        runRealApiLogicForCreate(classId, request);
    }

    private void runRealApiLogicForNotificationList(int classId) {
        apiService.getNotifications(classId).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                isNotificationListLoading.postValue(false);
                if (response.isSuccessful()) {
                    notificationListLiveData.postValue(response.body());
                } else {
                    notificationListError.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                isNotificationListLoading.postValue(false);
                notificationListError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    private void runRealApiLogicForDetail(int notificationId) {
        apiService.getNotificationDetail(notificationId).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                isDetailLoading.postValue(false);
                if (response.isSuccessful()) {
                    notificationDetailLiveData.postValue(response.body());
                } else {
                    detailError.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                isDetailLoading.postValue(false);
                detailError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    private void runRealApiLogicForCreate(int classId, NotificationRequest request) {
        apiService.createNotification(classId, request).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                isCreating.postValue(false);
                if (response.isSuccessful()) {
                    createSuccessEvent.postValue(response.body());
                } else {
                    createErrorEvent.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                isCreating.postValue(false);
                createErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public LiveData<List<Notification>> getNotificationList() {
        return notificationListLiveData;
    }
    public LiveData<Boolean> getIsNotificationListLoading() {
        return isNotificationListLoading;
    }
    public LiveData<String> getNotificationListError() {
        return notificationListError;
    }

    public LiveData<Notification> getNotificationDetail() {
        return notificationDetailLiveData;
    }
    public LiveData<Boolean> getIsDetailLoading() {
        return isDetailLoading;
    }
    public LiveData<String> getDetailError() {
        return detailError;
    }

    public LiveData<Notification> getCreateSuccessEvent() { return createSuccessEvent; }
    public LiveData<String> getCreateErrorEvent() { return createErrorEvent; }
    public LiveData<Boolean> getIsCreating() { return isCreating; }
}
package com.example.studymate.data.repository; // (Package của bạn)

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.Notification;
import com.example.studymate.data.model.request.NotificationRequest;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {

    private ApiService apiService;
    private final boolean IS_MOCK_MODE = false; // Vẫn dùng Mock

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

    // ⭐️ HÀM MỚI:
    public void fetchNotificationList(int classId) {
        isNotificationListLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForNotificationList(classId);
        } else {
            runRealApiLogicForNotificationList(classId);
        }
    }

    public void fetchNotificationDetail(int notificationId) {
        isDetailLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForDetail(notificationId);
        } else {
            runRealApiLogicForDetail(notificationId);
        }
    }

    public void createNotification(int classId, String title, String content) {
        isCreating.postValue(true);
        NotificationRequest request = new NotificationRequest(title, content);

        if (IS_MOCK_MODE) {
            runMockLogicForCreate(request);
        } else {
            runRealApiLogicForCreate(classId, request);
        }
    }

    // ⭐️ HÀM MỚI: (Mock logic)
    private void runMockLogicForNotificationList(int classId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ArrayList<Notification> mockList = new ArrayList<>();
            mockList.add(new Notification(1, "Nghỉ lễ 30/4", "Toàn trường nghỉ lễ...", "25/04/2025"));
            mockList.add(new Notification(2, "Thi giữa kỳ", "Lịch thi giữa kỳ...", "20/04/2025"));

            isNotificationListLoading.postValue(false);
            notificationListLiveData.postValue(mockList);
        }, 1000); // Trì hoãn 1 giây
    }

    // ⭐️ HÀM MỚI: (Real API logic)
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

    private void runMockLogicForDetail(int notificationId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Giả lập tìm thấy thông báo
            Notification mockDetail = new Notification(
                    notificationId,
                    "Tiêu đề chi tiết (Mock)",
                    "Đây là nội dung chi tiết (mock) cho thông báo ID " + notificationId,
                    "26/04/2025"
            );

            isDetailLoading.postValue(false);
            notificationDetailLiveData.postValue(mockDetail);
        }, 1000); // Trì hoãn 1 giây
    }

    // ⭐️ THÊM HÀM MỚI: (Real API logic)
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

    private void runMockLogicForCreate(NotificationRequest request) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Notification newNotif = new Notification(99, request.getTitle(), request.getContent(), "2025-11-15T11:00:00");
            isCreating.postValue(false);
            createSuccessEvent.postValue(newNotif);
        }, 1500);
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

    // ⭐️ THÊM GETTERS MỚI:
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
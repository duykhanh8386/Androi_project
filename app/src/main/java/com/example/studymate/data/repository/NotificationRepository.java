package com.example.studymate.data.repository; // (Package của bạn)

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.Notification;
import com.example.studymate.data.model.request.NotificationRequest;
import com.example.studymate.data.model.response.MessageResponse;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {

    private ApiService apiService;
    private final boolean IS_MOCK_MODE = true; // Vẫn dùng Mock

    // --- LiveData cho DANH SÁCH (List) ---
    private MutableLiveData<List<Notification>> notificationListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNotificationListLoading = new MutableLiveData<>();
    private MutableLiveData<String> notificationListError = new MutableLiveData<>();

    // --- LiveData cho CHI TIẾT (Detail) ---
    private MutableLiveData<Notification> notificationDetailLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDetailLoading = new MutableLiveData<>();
    private MutableLiveData<String> detailError = new MutableLiveData<>();

    // --- LiveData cho TẠO MỚI (Create) ---
    private MutableLiveData<Boolean> isCreateLoading = new MutableLiveData<>();
    private MutableLiveData<MessageResponse> createSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> createErrorEvent = new MutableLiveData<>();


    public NotificationRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // ========== 1. LẤY DANH SÁCH (BỊ THIẾU) ==========

    public void fetchNotificationList(int classId) {
        isNotificationListLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForNotificationList(classId);
        } else {
            runRealApiLogicForNotificationList(classId);
        }
    }

    private void runMockLogicForNotificationList(int classId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ArrayList<Notification> mockList = new ArrayList<>();
            mockList.add(new Notification(1, "Nghỉ lễ 30/4", "Toàn trường nghỉ lễ...", "25/04/2025"));
            mockList.add(new Notification(2, "Thi giữa kỳ", "Lịch thi giữa kỳ...", "20/04/2025"));

            isNotificationListLoading.postValue(false);
            notificationListLiveData.postValue(mockList);
        }, 1000); // Trì hoãn 1 giây
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

    // ========== 2. LẤY CHI TIẾT (BỊ THIẾU) ==========

    public void fetchNotificationDetail(int notificationId) {
        isDetailLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForDetail(notificationId);
        } else {
            runRealApiLogicForDetail(notificationId);
        }
    }

    private void runMockLogicForDetail(int notificationId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
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

    // ========== 3. TẠO MỚI (ĐÃ CÓ) ==========

    public void createNotification(int classId, NotificationRequest request) {
        isCreateLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForCreate(classId, request);
        } else {
            runRealApiLogicForCreate(classId, request);
        }
    }

    private void runRealApiLogicForCreate(int classId, NotificationRequest request) {
        apiService.createNotification(classId, request).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                isCreateLoading.postValue(false);
                if (response.isSuccessful()) {
                    createSuccessEvent.postValue(new MessageResponse("Gửi thông báo thành công!"));
                } else {
                    try {
                        String errorMsg = response.errorBody() != null ? response.errorBody().string() : "Lỗi không xác định";
                        createErrorEvent.postValue("Lỗi " + response.code() + ": " + errorMsg);
                    } catch (IOException e) {
                        createErrorEvent.postValue("Lỗi " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                isCreateLoading.postValue(false);
                createErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    private void runMockLogicForCreate(int classId, NotificationRequest request) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isCreateLoading.postValue(false);
            createSuccessEvent.postValue(new MessageResponse("Gửi thành công (Mock)"));
        }, 800); // Trễ 0.8 giây
    }


    // --- GETTERS (Đã gộp) ---

    // Getters cho DANH SÁCH (List)
    public LiveData<List<Notification>> getNotificationList() {
        return notificationListLiveData;
    }
    public LiveData<Boolean> getIsNotificationListLoading() {
        return isNotificationListLoading;
    }
    public LiveData<String> getNotificationListError() {
        return notificationListError;
    }

    // Getters cho CHI TIẾT (Detail)
    public LiveData<Notification> getNotificationDetail() {
        return notificationDetailLiveData;
    }
    public LiveData<Boolean> getIsDetailLoading() {
        return isDetailLoading;
    }
    public LiveData<String> getDetailError() {
        return detailError;
    }

    // Getters cho TẠO MỚI (Create)
    public LiveData<Boolean> getIsCreateLoading() {
        return isCreateLoading;
    }
    public LiveData<MessageResponse> getCreateSuccessEvent() {
        return createSuccessEvent;
    }
    public LiveData<String> getCreateErrorEvent() {
        return createErrorEvent;
    }
}
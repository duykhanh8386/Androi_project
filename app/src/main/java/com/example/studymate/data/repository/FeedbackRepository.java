package com.example.studymate.data.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.constants.RoleConstant;
import com.example.studymate.data.model.Feedback;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.User;
import com.example.studymate.data.model.request.FeedbackRequest;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import java.io.IOException; // ⭐️ THÊM
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackRepository {

    private ApiService apiService;
    private final boolean IS_MOCK_MODE = true; // (Giữ mock)

    // (LiveData cho Màn hình B - Chat)
    private MutableLiveData<List<Feedback>> feedbackThread = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoadingThread = new MutableLiveData<>();
    private MutableLiveData<String> threadError = new MutableLiveData<>();

    private MutableLiveData<Boolean> sendSuccessLoading = new MutableLiveData<>();
    private MutableLiveData<Feedback> sendSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> sendErrorEvent = new MutableLiveData<>();

    // (LiveData cho Màn hình A - Danh sách)
    private MutableLiveData<List<Feedback>> feedbackList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoadingList = new MutableLiveData<>();
    private MutableLiveData<String> listError = new MutableLiveData<>();


    public FeedbackRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // --- Màn hình A: Lấy danh sách ---
    public void fetchTeacherFeedbackList(int classId) {
        isLoadingList.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForList(classId);
        } else {
            runRealApiLogicForList(classId);
        }
    }

    private void runMockLogicForList(int classId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ArrayList<Feedback> mockList = new ArrayList<>();

            // (Dùng default constructor và setters cho an toàn)
            Feedback f1 = new Feedback();
            f1.setFeedbackId(10);
            f1.setFeedbackContent("Em chào cô, bài này làm sao ạ?");
            f1.setCreatedAt("2025-11-15T10:00:00");
            f1.setSenderId(201L);
            f1.setSenderName("Nguyễn Văn An");
            f1.setSenderUsername("20201111"); // (Mã SV)
            f1.setReceiverId(10L); // (ID Giáo viên)
            f1.setConversationId("conv_1");
            f1.setRead(false); // (Chưa đọc)
            mockList.add(f1);

            Feedback f2 = new Feedback();
            f2.setFeedbackId(12);
            f2.setFeedbackContent("Ok cô, em cảm ơn.");
            f2.setCreatedAt("2025-11-14T08:00:00");
            f2.setSenderId(202L);
            f2.setSenderName("Trần Thị Bình");
            f2.setSenderUsername("20202222"); // (Mã SV)
            f2.setReceiverId(10L); // (ID Giáo viên)
            f2.setConversationId("conv_2");
            f2.setRead(true); // (Đã đọc)
            mockList.add(f2);

            isLoadingList.postValue(false);
            feedbackList.postValue(mockList);
        }, 1000);
    }

    private void runRealApiLogicForList(int classId) {
        apiService.getTeacherFeedbackList(classId).enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                isLoadingList.postValue(false);
                if (response.isSuccessful()) {
                    feedbackList.postValue(response.body());
                } else {
                    listError.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                isLoadingList.postValue(false);
                listError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // --- Màn hình B: Lấy chi tiết cuộc trò chuyện ---
    public void getFeedbackThread(int classId, @Nullable Long studentId) {
        isLoadingThread.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForThread(classId, studentId);
        } else {
            runRealApiLogicForThread(classId, studentId);
        }
    }

    private void runMockLogicForThread(int classId, @Nullable Long studentId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ArrayList<Feedback> mockThread = new ArrayList<>();
            Long sId = (studentId != null ? studentId : 201L);

            // (Tin nhắn 1 - CỦA HỌC SINH)
            Feedback f1 = new Feedback();
            f1.setFeedbackId(10);
            f1.setFeedbackContent("Em chào cô, bài này làm sao ạ?");
            f1.setCreatedAt("2025-11-15T10:00:00");
            f1.setSenderId(sId); // ⭐️ Gửi từ Học sinh
            f1.setSenderName("Nguyễn Văn An");
            f1.setReceiverId(10L); // (Gửi cho GV)
            mockThread.add(f1);

            // ⭐️ LỖI CỦA BẠN LÀ Ở ĐÂY (Bạn đã copy-paste tin nhắn 1)
            // (Tin nhắn 2 - CỦA GIÁO VIÊN)
            Feedback f2 = new Feedback();
            f2.setFeedbackId(11);
            f2.setFeedbackContent("Em xem lại slide bài giảng nhé.");
            f2.setCreatedAt("2025-11-15T10:05:00");
            f2.setSenderId(10L); // ⭐️ Gửi từ Giáo viên
            f2.setSenderName("Giáo Viên B");
            f2.setReceiverId(sId); // (Gửi cho Học sinh)
            mockThread.add(f2);

            isLoadingThread.postValue(false);
            feedbackThread.postValue(mockThread);
        }, 1000);
    }

    private void runRealApiLogicForThread(int classId, @Nullable Long studentId) {
        apiService.getFeedbackThread(classId, studentId).enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                isLoadingThread.postValue(false);
                if (response.isSuccessful()) {
                    feedbackThread.postValue(response.body());
                } else {
                    threadError.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                isLoadingThread.postValue(false);
                threadError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // --- Màn hình B: Gửi tin nhắn ---

    // ⭐️ SỬA LẠI: Nhận FeedbackRequest
    public void sendFeedback(FeedbackRequest request) {
        sendSuccessLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForSend(request);
        } else {
            runRealApiLogicForSend(request);
        }
    }

    private void runMockLogicForSend(FeedbackRequest request) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Feedback newFeedback = new Feedback();
            newFeedback.setFeedbackId((int) (Math.random() * 1000));
            newFeedback.setFeedbackContent(request.getFeedbackContent());
            newFeedback.setCreatedAt("2025-11-15T11:00:00");
            newFeedback.setSenderId(10L); // (Giả sử 10L là ID Giáo viên)
            newFeedback.setReceiverId(request.getReceiverId()); // (Gửi cho Học sinh)

            sendSuccessLoading.postValue(false);
            sendSuccessEvent.postValue(newFeedback);
        }, 1000); // Trì hoãn 1 giây
    }

    // ⭐️ SỬA LẠI: Nhận FeedbackRequest
    private void runRealApiLogicForSend(FeedbackRequest request) {
        apiService.sendFeedback(request).enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                sendSuccessLoading.postValue(false);
                if (response.isSuccessful()) {
                    sendSuccessEvent.postValue(response.body());
                } else {
                    try {
                        sendErrorEvent.postValue(new com.google.gson.Gson().fromJson(response.errorBody().string(), Feedback.class).getFeedbackContent());
                    } catch (IOException e) {
                        sendErrorEvent.postValue("Lỗi: " + response.code());
                    }
                }
            }
            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {
                sendSuccessLoading.postValue(false);
                sendErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // --- Getters ---

    // (Getters cho Màn hình A)
    public LiveData<List<Feedback>> getFeedbackList() { return feedbackList; }
    public LiveData<Boolean> getIsLoadingList() { return isLoadingList; }
    public LiveData<String> getListError() { return listError; }

    // (Getters cho Màn hình B)
    public LiveData<List<Feedback>> getFeedbackThread() { return feedbackThread; }
    public LiveData<Boolean> getIsLoadingThread() { return isLoadingThread; }
    public LiveData<String> getThreadError() { return threadError; }

    public LiveData<Boolean> getSendLoading() { return sendSuccessLoading; }
    public LiveData<Feedback> getSendSuccessEvent() { return sendSuccessEvent; }
    public LiveData<String> getSendErrorEvent() { return sendErrorEvent; }
}
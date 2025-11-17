package com.example.studymate.data.repository;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.Feedback;
import com.example.studymate.data.model.request.FeedbackRequest;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackRepository {

    private ApiService apiService;

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

    public void fetchTeacherFeedbackList(int classId) {
        isLoadingList.postValue(true);
        runRealApiLogicForList(classId);
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

    public void getFeedbackThread(int classId, @Nullable Long studentId) {
        isLoadingThread.postValue(true);
        runRealApiLogicForThread(classId, studentId);
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

    public void sendFeedback(FeedbackRequest request) {
        sendSuccessLoading.postValue(true);
        runRealApiLogicForSend(request);
    }

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
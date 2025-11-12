package com.example.studymate.data.repository;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.studymate.data.model.User;
import com.example.studymate.data.model.request.ApprovalRequest;
import com.example.studymate.data.model.response.MessageResponse;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherRepository {

    private ApiService apiService;
    private final boolean IS_MOCK_MODE = true;

    // LiveData cho danh sách chờ
    private MutableLiveData<List<User>> pendingListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    // LiveData cho sự kiện Phê duyệt/Từ chối (để báo cho Fragment)
    private MutableLiveData<String> approvalSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> approvalErrorEvent = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public TeacherRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // --- Lấy danh sách chờ ---
    public void fetchPendingList(int classId) {
        isLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForList(classId);
        } else {
            runRealApiLogicForList(classId);
        }
    }

    private void runMockLogicForList(int classId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ArrayList<User> mockList = new ArrayList<>();
            // (Mock User: id, fullName, email, role)
            // (Quan trọng: ID ở đây là *studentClassId* (ID bảng trung gian), không phải UserID)
            mockList.add(new User(1, "Học Sinh Chờ 1", "wait1@test.com", "STUDENT"));
            mockList.add(new User(2, "Học Sinh Chờ 2", "wait2@test.com", "STUDENT"));
            isLoading.postValue(false);
            pendingListLiveData.postValue(mockList);
        }, 1000);
    }

    private void runRealApiLogicForList(int classId) {
        apiService.getPendingStudents(classId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful()) {
                    pendingListLiveData.postValue(response.body());
                } else {
                    error.postValue("Lỗi tải danh sách: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                isLoading.postValue(false);
                error.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // --- Xử lý Phê duyệt/Từ chối ---

    // (Helper chung)
    private void processApproval(int studentClassId, String status) {
        isUpdating.postValue(true);
        if (IS_MOCK_MODE) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                isUpdating.postValue(false);
                approvalSuccessEvent.postValue(status + " thành công!");
            }, 500); // Giả lập 0.5 giây
        } else {
            ApprovalRequest request = new ApprovalRequest(status);
            apiService.approveOrRejectStudent(studentClassId, request).enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                    isUpdating.postValue(false);
                    if (response.isSuccessful() && response.body() != null) {
                        approvalSuccessEvent.postValue(response.body().getMessage());
                    } else {
                        approvalErrorEvent.postValue("Lỗi: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<MessageResponse> call, Throwable t) {
                    isUpdating.postValue(false);
                    approvalErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
                }
            });
        }
    }

    // (Hàm public)
    public void approveStudent(int studentClassId) {
        processApproval(studentClassId, "APPROVED");
    }

    public void rejectStudent(int studentClassId) {
        processApproval(studentClassId, "REJECTED");
    }

    // --- Getters ---
    public LiveData<List<User>> getPendingList() { return pendingListLiveData; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsUpdating() { return isUpdating; }
    public LiveData<String> getApprovalSuccessEvent() { return approvalSuccessEvent; }
    public LiveData<String> getApprovalErrorEvent() { return approvalErrorEvent; }
}
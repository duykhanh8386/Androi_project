package com.example.studymate.data.repository;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.StudentClass;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.request.UpdateClassRequest;
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
    private MutableLiveData<List<StudentClass>> pendingListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    // LiveData cho sự kiện Phê duyệt/Từ chối (để báo cho Fragment)
    private MutableLiveData<String> approvalSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> approvalErrorEvent = new MutableLiveData<>();
    private MutableLiveData<Boolean> isApprovalLoading = new MutableLiveData<>();

    // LiveData cho sự kiện (HÀNG LOẠT)
    private MutableLiveData<Boolean> isBulkLoading = new MutableLiveData<>();
    private MutableLiveData<String> bulkSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> bulkErrorEvent = new MutableLiveData<>();

    // LiveData cho sự kiện Tạo lớp
    private MutableLiveData<StudyClass> createClassSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> createClassErrorEvent = new MutableLiveData<>();
    private MutableLiveData<Boolean> isCreatingClass = new MutableLiveData<>();

    // LiveData cho sự kiện CẬP NHẬT LỚP
    private MutableLiveData<StudyClass> updateClassSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> updateClassErrorEvent = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdatingClass = new MutableLiveData<>();

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
            ArrayList<StudentClass> mockList = new ArrayList<>();
            // (Mock User: id, fullName, email, role)
            // (Quan trọng: ID ở đây là *studentClassId* (ID bảng trung gian), không phải UserID)
//            mockList.add(new User(1, "Học Sinh Chờ 1", "wait1@test.com", "STUDENT"));
//            mockList.add(new User(2, "Học Sinh Chờ 2", "wait2@test.com", "STUDENT"));
            isLoading.postValue(false);
            pendingListLiveData.postValue(mockList);
        }, 1000);
    }

    private void runRealApiLogicForList(int classId) {
        apiService.getPendingStudents(classId).enqueue(new Callback<List<StudentClass>>() {
            @Override
            public void onResponse(Call<List<StudentClass>> call, Response<List<StudentClass>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful()) {
                    pendingListLiveData.postValue(response.body());
                } else {
                    error.postValue("Lỗi tải danh sách: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<StudentClass>> call, Throwable t) {
                isLoading.postValue(false);
                error.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // --- Xử lý Phê duyệt/Từ chối đơn le ---

    // (Helper chung)
    private void processApproval(int studentClassId, String status) {
        isApprovalLoading.postValue(true);
        if (IS_MOCK_MODE) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                isApprovalLoading.postValue(false);
                approvalSuccessEvent.postValue(status + " thành công!");
            }, 500);
        } else {
            apiService.approveOrRejectStudent(studentClassId, status).enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                    isApprovalLoading.postValue(false);
                    if (response.isSuccessful() && response.body() != null) {
                        approvalSuccessEvent.postValue("Phê duyệt thành công");
                    } else {
                        approvalErrorEvent.postValue("Lỗi: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<MessageResponse> call, Throwable t) {
                    isApprovalLoading.postValue(false);
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

    // (Phê duyệt/Từ chối - HÀNG LOẠT)
    public void approveAllPending(int classId) {
        isBulkLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForApproveAll(classId);
        } else {
            // ⭐️ SỬA LẠI:
            runRealApiLogicForUpdateAll(classId, "APPROVED");
        }
    }

    public void rejectAllPending(int classId) {
        isBulkLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForRejectAll(classId);
        } else {
            // ⭐️ SỬA LẠI:
            runRealApiLogicForUpdateAll(classId, "REJECTED");
        }
    }

    // ⭐️ THÊM MỚI: Logic Mock (Hàng loạt)
    private void runMockLogicForApproveAll(int classId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isBulkLoading.postValue(false);
            bulkSuccessEvent.postValue("Đã phê duyệt tất cả!");
        }, 1500);
    }
    private void runMockLogicForRejectAll(int classId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isBulkLoading.postValue(false);
            bulkSuccessEvent.postValue("Đã từ chối tất cả!");
        }, 1500);
    }

    // ⭐️ THÊM MỚI: Logic API (Hàng loạt)
    private void runRealApiLogicForUpdateAll(int classId, String status) {
        // Gọi hàm mới trong ApiService
        apiService.updateAllPendingStatus(classId, status).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                isBulkLoading.postValue(false);
                if(response.isSuccessful() && response.body() != null) {
                    bulkSuccessEvent.postValue(response.body().getMessage());
                } else {
                    bulkErrorEvent.postValue("Lỗi xử lý hàng loạt");
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                isBulkLoading.postValue(false);
                bulkErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // ⭐️ THÊM HÀM MỚI: Tạo lớp
    public void createClass(String className, String classTime) {
        isCreatingClass.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForCreateClass(className, classTime);
        } else {
            runRealApiLogicForCreateClass(className, classTime);
        }
    }

    private void runMockLogicForCreateClass(String className, String classTime) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Giả lập tạo thành công
            StudyClass newClass = new StudyClass(99, className, classTime);
            isCreatingClass.postValue(false);
            createClassSuccessEvent.postValue(newClass);
        }, 1500); // Trì hoãn 1.5 giây
    }

    private void runRealApiLogicForCreateClass(String className, String classTime) {
        UpdateClassRequest request = new UpdateClassRequest(className, classTime);
        apiService.createClass(request).enqueue(new Callback<StudyClass>() {
            @Override
            public void onResponse(Call<StudyClass> call, Response<StudyClass> response) {
                isCreatingClass.postValue(false);
                if (response.isSuccessful()) {
                    createClassSuccessEvent.postValue(response.body());
                } else {
                    createClassErrorEvent.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<StudyClass> call, Throwable t) {
                isCreatingClass.postValue(false);
                createClassErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public void updateClass(int classId, String className, String classTime) {
        isUpdatingClass.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForUpdateClass(classId, className, classTime);
        } else {
            runRealApiLogicForUpdateClass(classId, className, classTime);
        }
    }

    private void runMockLogicForUpdateClass(int classId, String className, String classTime) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Giả lập cập nhật thành công
            StudyClass updatedClass = new StudyClass(classId, className, classTime);
            isUpdatingClass.postValue(false);
            updateClassSuccessEvent.postValue(updatedClass);
        }, 1500); // Trì hoãn 1.5 giây
    }

    private void runRealApiLogicForUpdateClass(int classId, String className, String classTime) {
        UpdateClassRequest request = new UpdateClassRequest(className, classTime);
        apiService.updateClass(classId, request).enqueue(new Callback<StudyClass>() {
            @Override
            public void onResponse(Call<StudyClass> call, Response<StudyClass> response) {
                isUpdatingClass.postValue(false);
                if (response.isSuccessful()) {
                    updateClassSuccessEvent.postValue(response.body());
                } else {
                    updateClassErrorEvent.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<StudyClass> call, Throwable t) {
                isUpdatingClass.postValue(false);
                updateClassErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // --- Getters ---
    public LiveData<List<StudentClass>> getPendingList() { return pendingListLiveData; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsApprovalLoading() { return isApprovalLoading; }
    public LiveData<String> getApprovalSuccessEvent() { return approvalSuccessEvent; }
    public LiveData<String> getApprovalErrorEvent() { return approvalErrorEvent; }

    public LiveData<Boolean> getIsBulkLoading() { return isBulkLoading; }
    public LiveData<String> getBulkSuccessEvent() { return bulkSuccessEvent; }
    public LiveData<String> getBulkErrorEvent() { return bulkErrorEvent; }

    public LiveData<StudyClass> getCreateClassSuccessEvent() { return createClassSuccessEvent; }
    public LiveData<String> getCreateClassErrorEvent() { return createClassErrorEvent; }
    public LiveData<Boolean> getIsCreatingClass() { return isCreatingClass; }

    public LiveData<StudyClass> getUpdateClassSuccessEvent() { return updateClassSuccessEvent; }
    public LiveData<String> getUpdateClassErrorEvent() { return updateClassErrorEvent; }
    public LiveData<Boolean> getIsUpdatingClass() { return isUpdatingClass; }
}
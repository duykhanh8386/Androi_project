package com.example.studymate.data.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.request.JoinClassRequest;
import com.example.studymate.data.model.response.MessageResponse;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassRepository {

    private ApiService apiService;
    private final boolean IS_MOCK_MODE = false; // ⭐️ Vẫn dùng Mock

    // LiveData cho danh sách lớp
    private MutableLiveData<List<StudyClass>> classListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> classListError = new MutableLiveData<>();

    // ⭐️ THÊM MỚI: LiveData cho "chi tiết"
    private MutableLiveData<StudyClass> classDetailLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDetailLoading = new MutableLiveData<>();
    private MutableLiveData<String> classDetailError = new MutableLiveData<>();

    // ⭐️ THÊM MỚI: LiveData cho sự kiện "Join Class"
    private MutableLiveData<String> joinClassSuccess = new MutableLiveData<>();
    private MutableLiveData<String> joinClassError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isJoinClassLoading = new MutableLiveData<>();

    public ClassRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // ViewModel sẽ gọi hàm này
    public void fetchStudentClasses() {
        isLoading.postValue(true); // Báo là đang tải

        if (IS_MOCK_MODE) {
            runMockLogic();
        } else {
            runRealApiLogic();
        }
    }

    public void fetchClassDetails(int classId) {
        isDetailLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForDetail(classId);
        } else {
            runRealApiLogicForDetail(classId);
        }
    }

    public void joinClass(String classCode) {
        isJoinClassLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForJoinClass(classCode);
        } else {
            runRealApiLogicForJoinClass(classCode);
        }
    }

    // Logic gọi API thật
    private void runRealApiLogic() {
        apiService.getStudentClasses().enqueue(new Callback<List<StudyClass>>() {
            @Override
            public void onResponse(Call<List<StudyClass>> call, Response<List<StudyClass>> response) {
                isLoading.postValue(false); // Tải xong
                if (response.isSuccessful()) {
                    classListLiveData.postValue(response.body());
                } else {
                    classListError.postValue("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<StudyClass>> call, Throwable t) {
                isLoading.postValue(false); // Tải xong
                classListError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // Logic dữ liệu mẫu (đã chuyển từ ViewModel cũ sang đây)
    private void runMockLogic() {
        // Giả lập độ trễ 1.5 giây
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ArrayList<StudyClass> mockList = new ArrayList<>();
            mockList.add(new StudyClass(1, "Toán 10A1", "GV: Nguyễn Văn A"));
            mockList.add(new StudyClass(2, "Lý 11B2", "GV: Trần Thị B"));
            mockList.add(new StudyClass(3, "Hóa 12C3", "GV: Lê Văn C"));
            mockList.add(new StudyClass(4, "Sinh 10A4", "GV: Phạm Thị D"));
            mockList.add(new StudyClass(5, "Anh 11E5", "GV: Bùi Văn E"));

            isLoading.postValue(false); // Tải xong
            classListLiveData.postValue(mockList); // Gửi dữ liệu
        }, 1500); // Trì hoãn 1.5 giây
    }

    private void runRealApiLogicForDetail(int classId) {
        apiService.getClassDetails(classId).enqueue(new Callback<StudyClass>() {
            @Override
            public void onResponse(Call<StudyClass> call, Response<StudyClass> response) {
                isDetailLoading.postValue(false);
                if (response.isSuccessful()) {
                    classDetailLiveData.postValue(response.body());
                } else {
                    classDetailError.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<StudyClass> call, Throwable t) {
                isDetailLoading.postValue(false);
                classDetailError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    private void runMockLogicForDetail(int classId) {
        // Giả lập độ trễ 1 giây
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Giả lập tìm thấy lớp học dựa trên ID
            // (id, className, classTime/teacherName)
            StudyClass mockClass = new StudyClass(classId, "Chi tiết lớp " + classId, "GV: Nguyễn Văn Z");

            isDetailLoading.postValue(false);
            classDetailLiveData.postValue(mockClass);
        }, 1000);
    }

    // ⭐️ THÊM HÀM MỚI:
    private void runRealApiLogicForJoinClass(String classCode) {
        JoinClassRequest request = new JoinClassRequest(classCode);
        apiService.joinClass(request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                isJoinClassLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    joinClassSuccess.postValue(response.body().getMessage());
                } else {
                    // (Nên parse error body ở đây)
                    try {
                        joinClassError.postValue(new com.google.gson.Gson().fromJson(response.errorBody().string(), MessageResponse.class).getMessage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                isJoinClassLoading.postValue(false);
                joinClassError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // ⭐️ THÊM HÀM MỚI: (Mock logic)
    private void runMockLogicForJoinClass(String classCode) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isJoinClassLoading.postValue(false);
            // Dựa trên Hình 2.7.11[cite: 377], dùng mã "IT202425314"
            if (classCode.equalsIgnoreCase("IT202425314")) {
                // Dựa trên Use Case 2.4.12[cite: 292], trả về thông báo
                joinClassSuccess.postValue("Yêu cầu tham gia lớp đã được gửi. Vui lòng chờ giáo viên duyệt!");
            } else {
                joinClassError.postValue("Mã lớp không hợp lệ (dữ liệu mẫu).");
            }
        }, 1500); // Trì hoãn 1.5 giây
    }

    // --- Getters ---
    public LiveData<List<StudyClass>> getClassListLiveData() {
        return classListLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getClassListError() {
        return classListError;
    }

    public LiveData<StudyClass> getClassDetailLiveData() {
        return classDetailLiveData;
    }
    public LiveData<Boolean> getIsDetailLoading() {
        return isDetailLoading;
    }
    public LiveData<String> getClassDetailError() {
        return classDetailError;
    }
    public LiveData<String> getJoinClassSuccess() {
        return joinClassSuccess;
    }
    public LiveData<String> getJoinClassError() {
        return joinClassError;
    }
    public LiveData<Boolean> getIsJoinClassLoading() {
        return isJoinClassLoading;
    }
}
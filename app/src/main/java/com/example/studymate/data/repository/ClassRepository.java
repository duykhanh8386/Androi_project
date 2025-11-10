package com.example.studymate.data.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassRepository {

    private ApiService apiService;
    private final boolean IS_MOCK_MODE = true; // ⭐️ Vẫn dùng Mock

    // LiveData cho danh sách lớp
    private MutableLiveData<List<StudyClass>> classListLiveData = new MutableLiveData<>();
    // LiveData cho trạng thái "đang tải"
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    // LiveData cho lỗi
    private MutableLiveData<String> classListError = new MutableLiveData<>();

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
}
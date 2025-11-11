package com.example.studymate.data.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.Grade;
import com.example.studymate.data.model.User;
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

public class StudentRepository {
    private ApiService apiService;
    private final boolean IS_MOCK_MODE = true; // ⭐️ Vẫn dùng Mock


    // LiveData cho sự kiện "Join Class"
    private MutableLiveData<String> joinClassSuccess = new MutableLiveData<>();
    private MutableLiveData<String> joinClassError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isJoinClassLoading = new MutableLiveData<>();

    // LiveData cho danh sách học sinh
    private MutableLiveData<List<User>> studentListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isStudentListLoading = new MutableLiveData<>();
    private MutableLiveData<String> studentListError = new MutableLiveData<>();

    // LiveData cho danh sách điểm
    private MutableLiveData<List<Grade>> gradeListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public StudentRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public void joinClass(String classCode) {
        isJoinClassLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForJoinClass(classCode);
        } else {
            runRealApiLogicForJoinClass(classCode);
        }
    }

    public void fetchStudentList(int classId) {
        isStudentListLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForStudentList(classId);
        } else {
            runRealApiLogicForStudentList(classId);
        }
    }

    public void fetchStudentGrades(int classId) {
        isLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForGrades(classId);
        } else {
            runRealApiLogicForGrades(classId);
        }
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

    private void runMockLogicForStudentList(int classId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ArrayList<User> mockStudents = new ArrayList<>();
            // (Dùng constructor User(id, fullName, email, role) bạn đã có)
            mockStudents.add(new User(2, "Nguyễn Văn A", "nva@test.com", "STUDENT"));
            mockStudents.add(new User(3, "Trần Thị B", "ttb@test.com", "STUDENT"));
            mockStudents.add(new User(4, "Lê Văn C", "lvc@test.com", "STUDENT"));
            mockStudents.add(new User(5, "Lê Văn D", "lvc@test.com", "STUDENT"));
            mockStudents.add(new User(6, "Trần Văn F", "lvc@test.com", "STUDENT"));
            mockStudents.add(new User(7, "Lê Văn C", "lvc@test.com", "STUDENT"));
            mockStudents.add(new User(8, "Lê Văn C", "lvc@test.com", "STUDENT"));
            mockStudents.add(new User(9, "Lê Văn C", "lvc@test.com", "STUDENT"));
            isStudentListLoading.postValue(false);
            studentListLiveData.postValue(mockStudents);
        }, 1000); // Trì hoãn 1 giây
    }

    private void runRealApiLogicForStudentList(int classId) {
        apiService.getStudentsInClass(classId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                isStudentListLoading.postValue(false);
                if (response.isSuccessful()) {
                    studentListLiveData.postValue(response.body());
                } else {
                    studentListError.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                isStudentListLoading.postValue(false);
                studentListError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // ⭐️ HÀM MỚI: (Mock logic)
    private void runMockLogicForGrades(int classId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ArrayList<Grade> mockList = new ArrayList<>();
            // (Dựa trên Grade POJO)
            // (Giả sử constructor là (id, type, score))
            mockList.add(new Grade(1, "TX", 8.0));
            mockList.add(new Grade(2, "TX", 8.5));
            mockList.add(new Grade(3, "GK", 9.0));
            mockList.add(new Grade(4, "CK", 10.0));

            isLoading.postValue(false);
            gradeListLiveData.postValue(mockList);
        }, 1000); // Trì hoãn 1 giây
    }

    // ⭐️ HÀM MỚI: (Real API logic)
    private void runRealApiLogicForGrades(int classId) {
        apiService.getStudentGrades(classId).enqueue(new Callback<List<Grade>>() {
            @Override
            public void onResponse(Call<List<Grade>> call, Response<List<Grade>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful()) {
                    gradeListLiveData.postValue(response.body());
                } else {
                    error.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Grade>> call, Throwable t) {
                isLoading.postValue(false);
                error.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // --- Getters ---
    public LiveData<String> getJoinClassSuccess() {
        return joinClassSuccess;
    }
    public LiveData<String> getJoinClassError() {
        return joinClassError;
    }
    public LiveData<Boolean> getIsJoinClassLoading() {
        return isJoinClassLoading;
    }

    public LiveData<List<User>> getStudentListLiveData() {
        return studentListLiveData;
    }
    public LiveData<Boolean> getIsStudentListLoading() {
        return isStudentListLoading;
    }
    public LiveData<String> getStudentListError() {
        return studentListError;
    }

    public LiveData<List<Grade>> getGradeList() {
        return gradeListLiveData;
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<String> getError() {
        return error;
    }
}

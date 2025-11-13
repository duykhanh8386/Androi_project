package com.example.studymate.data.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.response.ClassDetailResponse;
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

    // LiveData cho danh sách lớp của học sinh
    private MutableLiveData<List<StudyClass>> studentClassListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isStudentClassListLoading = new MutableLiveData<>();
    private MutableLiveData<String> studentClassListError = new MutableLiveData<>();

    // LiveData cho danh sách lớp của giáo viên
    private MutableLiveData<List<StudyClass>> teacherClassListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTeacherClassListLoading = new MutableLiveData<>();
    private MutableLiveData<String> teacherClassListError = new MutableLiveData<>();

    // LiveData cho chi tiết lớp học
    private MutableLiveData<ClassDetailResponse> classDetailLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDetailLoading = new MutableLiveData<>();
    private MutableLiveData<String> classDetailError = new MutableLiveData<>();

    // LiveData cho sự kiện "Rời lớp"
    private MutableLiveData<Boolean> isLeaveLoading = new MutableLiveData<>();
    private MutableLiveData<String> leaveSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> leaveErrorEvent = new MutableLiveData<>();


    public ClassRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // ViewModel sẽ gọi hàm này
    public void fetchStudentClasses() {
        isStudentClassListLoading.postValue(true); // Báo là đang tải

        if (IS_MOCK_MODE) {
            runMockLogic();
        } else {
            runRealApiLogic();
        }
    }

    public void fetchTeacherClasses() {
        isTeacherClassListLoading.postValue(true); // Báo là đang tải

        if (IS_MOCK_MODE) {
            runMockLogicForClassListTeacher();
        } else {
            runRealApiLogicForClassListTeacher();
        }
    }

    private void runRealApiLogicForClassListTeacher() {
        // Giả lập độ trễ 1.5 giây
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ArrayList<StudyClass> mockList = new ArrayList<>();
            mockList.add(new StudyClass(1, "Toán 10A1", "GV: Nguyễn Văn A"));
            mockList.add(new StudyClass(2, "Lý 11B2", "GV: Trần Thị B"));
            mockList.add(new StudyClass(3, "Hóa 12C3", "GV: Lê Văn C"));
            mockList.add(new StudyClass(4, "Sinh 10A4", "GV: Phạm Thị D"));
            mockList.add(new StudyClass(5, "Anh 11E5", "GV: Bùi Văn E"));

            isTeacherClassListLoading.postValue(false); // Tải xong
            teacherClassListLiveData.postValue(mockList); // Gửi dữ liệu
        }, 1500); // Trì hoãn 1.5 giây
    }

    private void runMockLogicForClassListTeacher() {
        apiService.getTeacherClasses().enqueue(new Callback<List<StudyClass>>() {
            @Override
            public void onResponse(Call<List<StudyClass>> call, Response<List<StudyClass>> response) {
                isTeacherClassListLoading.postValue(false); // Tải xong
                if (response.isSuccessful()) {
                    teacherClassListLiveData.postValue(response.body());
                } else {
                    teacherClassListError.postValue("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<StudyClass>> call, Throwable t) {
                isTeacherClassListLoading.postValue(false); // Tải xong
                teacherClassListError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public void fetchClassDetails(int classId) {
        isDetailLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForDetail(classId);
        } else {
            runRealApiLogicForDetail(classId);
        }
    }

    public void leaveClass(int classId) {
        isLeaveLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForLeaveClass(classId);
        } else {
            runRealApiLogicForLeaveClass(classId);
        }
    }

    // Logic gọi API thật
    private void runRealApiLogic() {
        apiService.getStudentClasses().enqueue(new Callback<List<StudyClass>>() {
            @Override
            public void onResponse(Call<List<StudyClass>> call, Response<List<StudyClass>> response) {
                isStudentClassListLoading.postValue(false); // Tải xong
                if (response.isSuccessful()) {
                    studentClassListLiveData.postValue(response.body());
                } else {
                    studentClassListError.postValue("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<StudyClass>> call, Throwable t) {
                isStudentClassListLoading.postValue(false); // Tải xong
                studentClassListError.postValue("Lỗi mạng: " + t.getMessage());
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

            isStudentClassListLoading.postValue(false); // Tải xong
            studentClassListLiveData.postValue(mockList); // Gửi dữ liệu
        }, 1500); // Trì hoãn 1.5 giây
    }

    private void runRealApiLogicForDetail(int classId) {
        apiService.getClassDetails(classId).enqueue(new Callback<ClassDetailResponse>() {
            @Override
            public void onResponse(Call<ClassDetailResponse> call, Response<ClassDetailResponse> response) {
                isDetailLoading.postValue(false);
                if (response.isSuccessful()) {
                    classDetailLiveData.postValue(response.body());
                } else {
                    classDetailError.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ClassDetailResponse> call, Throwable t) {
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
//            StudyClass mockClass = new StudyClass(classId, "Chi tiết lớp " + classId, "GV: Nguyễn Văn Z");
            ClassDetailResponse mockClass = new ClassDetailResponse(3, "Lớp 10A1", "L10A1",
                    "Thứ 2 - 7h30", "Teacher User", 35);
            isDetailLoading.postValue(false);
            classDetailLiveData.postValue(mockClass);
        }, 1000);
    }

    // ⭐️ THÊM HÀM MỚI: (Mock logic)
    private void runMockLogicForLeaveClass(int classId) {
        // Giả lập độ trễ 1 giây
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isLeaveLoading.postValue(false);
            // Trả về thông báo thành công (theo Use Case 2.4.13)
            leaveSuccessEvent.postValue("Rời lớp thành công!");
        }, 1000);
    }

    // ⭐️ THÊM HÀM MỚI: (Real API logic)
    private void runRealApiLogicForLeaveClass(int classId) {
        apiService.leaveClass(classId).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                isLeaveLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    leaveSuccessEvent.postValue(response.body().getMessage());
                } else {
                    try {
                        leaveErrorEvent.postValue(new com.google.gson.Gson().fromJson(response.errorBody().string(), MessageResponse.class).getMessage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                isLeaveLoading.postValue(false);
                leaveErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // --- Getters ---
    public LiveData<List<StudyClass>> getStudentClassListLiveData() {
        return studentClassListLiveData;
    }

    public LiveData<Boolean> getIsStudentClassListLoading() {
        return isStudentClassListLoading;
    }

    public LiveData<String> getStudentClassListError() {
        return studentClassListError;
    }

    public LiveData<List<StudyClass>> getTeacherClassListLiveData() {
        return studentClassListLiveData;
    }

    public LiveData<Boolean> getIsTeacherClassListLoading() {
        return isTeacherClassListLoading;
    }

    public LiveData<String> getTeacherClassListError() {
        return teacherClassListError;
    }

    public LiveData<ClassDetailResponse> getClassDetailLiveData() {
        return classDetailLiveData;
    }
    public LiveData<Boolean> getIsDetailLoading() {
        return isDetailLoading;
    }
    public LiveData<String> getClassDetailError() {
        return classDetailError;
    }

    public LiveData<Boolean> getIsLeaveLoading() {
        return isLeaveLoading;
    }
    public LiveData<String> getLeaveSuccessEvent() {
        return leaveSuccessEvent;
    }
    public LiveData<String> getLeaveErrorEvent() {
        return leaveErrorEvent;
    }
}
package com.example.studymate.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.response.ClassDetailResponse;
import com.example.studymate.data.model.response.MessageResponse;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassRepository {

    private ApiService apiService;

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

    // LiveData cho sự kiện "XÓA LỚP"
    private MutableLiveData<Boolean> isDeletingClass = new MutableLiveData<>();
    private MutableLiveData<String> deleteSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> deleteErrorEvent = new MutableLiveData<>();

    public ClassRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public void fetchStudentClasses() {
        isStudentClassListLoading.postValue(true);
        runRealApiLogic();
    }

    public void fetchTeacherClasses() {
        isTeacherClassListLoading.postValue(true);
        runRealApiLogicForClassListTeacher();
    }

    public void fetchClassDetails(int classId) {
        isDetailLoading.postValue(true);
        runRealApiLogicForDetail(classId);
    }

    public void leaveClass(int classId) {
        isLeaveLoading.postValue(true);
        runRealApiLogicForLeaveClass(classId);
    }
    public void deleteClass(int classId) {
        isDeletingClass.postValue(true);
        runRealApiLogicForDeleteClass(classId);
    }

    private void runRealApiLogicForClassListTeacher() {
        apiService.getTeacherClasses().enqueue(new Callback<List<StudyClass>>() {
            @Override
            public void onResponse(Call<List<StudyClass>> call, Response<List<StudyClass>> response) {
                isTeacherClassListLoading.postValue(false);
                if (response.isSuccessful()) {
                    teacherClassListLiveData.postValue(response.body());
                } else {
                    teacherClassListError.postValue("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<StudyClass>> call, Throwable t) {
                isTeacherClassListLoading.postValue(false);
                teacherClassListError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    private void runRealApiLogic() {
        apiService.getStudentClasses().enqueue(new Callback<List<StudyClass>>() {
            @Override
            public void onResponse(Call<List<StudyClass>> call, Response<List<StudyClass>> response) {
                isStudentClassListLoading.postValue(false);
                if (response.isSuccessful()) {
                    studentClassListLiveData.postValue(response.body());
                } else {
                    studentClassListError.postValue("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<StudyClass>> call, Throwable t) {
                isStudentClassListLoading.postValue(false);
                studentClassListError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
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

    private void runRealApiLogicForDeleteClass(int classId) {
        apiService.deleteClass(classId).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                isDeletingClass.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    deleteSuccessEvent.postValue(response.body().getMessage());
                } else {
                    try {
                        deleteErrorEvent.postValue(new com.google.gson.Gson().fromJson(response.errorBody().string(), MessageResponse.class).getMessage());
                    } catch (IOException e) {
                        deleteErrorEvent.postValue("Lỗi: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                isDeletingClass.postValue(false);
                deleteErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
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
        return teacherClassListLiveData;
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

    public LiveData<Boolean> getIsDeletingClass() {
        return isDeletingClass;
    }
    public LiveData<String> getDeleteSuccessEvent() {
        return deleteSuccessEvent;
    }
    public LiveData<String> getDeleteErrorEvent() {
        return deleteErrorEvent;
    }
}
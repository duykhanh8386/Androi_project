package com.example.studymate.data.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.Grade;
import com.example.studymate.data.model.request.JoinClassRequest;
import com.example.studymate.data.model.response.MessageResponse;
import com.example.studymate.data.model.response.StudentResponse;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRepository {
    private ApiService apiService;

    // LiveData cho sự kiện "Join Class"
    private MutableLiveData<String> joinClassSuccess = new MutableLiveData<>();
    private MutableLiveData<String> joinClassError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isJoinClassLoading = new MutableLiveData<>();

    // LiveData cho danh sách học sinh
    private MutableLiveData<List<StudentResponse>> studentListLiveData = new MutableLiveData<>();
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
        runRealApiLogicForJoinClass(classCode);
    }

    public void fetchStudentList(int classId) {
        isStudentListLoading.postValue(true);
        runRealApiLogicForStudentList(classId);
    }

    public void fetchStudentGrades(int classId) {
        isLoading.postValue(true);
        runRealApiLogicForGrades(classId);
    }

    private void runRealApiLogicForJoinClass(String classCode) {
        JoinClassRequest request = new JoinClassRequest(classCode);
        apiService.joinClass(request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                isJoinClassLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    joinClassSuccess.postValue(response.body().getMessage());
                } else {
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

    private void runRealApiLogicForStudentList(int classId) {
        apiService.getStudentsInClass(classId).enqueue(new Callback<List<StudentResponse>>() {
            @Override
            public void onResponse(Call<List<StudentResponse>> call, Response<List<StudentResponse>> response) {
                isStudentListLoading.postValue(false);
                if (response.isSuccessful()) {
                    studentListLiveData.postValue(response.body());
                } else {
                    studentListError.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<StudentResponse>> call, Throwable t) {
                isStudentListLoading.postValue(false);
                studentListError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

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

    public LiveData<List<StudentResponse>> getStudentListLiveData() {
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

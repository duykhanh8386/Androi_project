package com.example.studymate.data.repository;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.Grade;
import com.example.studymate.data.model.StudentClass;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.request.GradeRequest;
import com.example.studymate.data.model.request.UpdateClassRequest;
import com.example.studymate.data.model.response.MessageResponse;
import com.example.studymate.data.model.response.StudentResponse;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherRepository {

    private ApiService apiService;

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

    // LiveData cho DANH SÁCH HỌC SINH (Quản lý)
    private MutableLiveData<List<StudentResponse>> studentListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isStudentListLoading = new MutableLiveData<>();
    private MutableLiveData<String> studentListError = new MutableLiveData<>();

    // LiveData cho sự kiện THÊM ĐIỂM
    private MutableLiveData<Grade> addGradeSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> addGradeErrorEvent = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAddingGrade = new MutableLiveData<>();

    // LiveData cho sự kiện SỬA ĐIỂM
    private MutableLiveData<Grade> updateGradeSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> updateGradeErrorEvent = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdatingGrade = new MutableLiveData<>();

    // LiveData cho XÓA ĐIỂM
    private MutableLiveData<MessageResponse> deleteGradeSuccessEvent = new MutableLiveData<>();
    private MutableLiveData<String> deleteGradeErrorEvent = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDeletingGrade = new MutableLiveData<>();

    public TeacherRepository() {
        this.apiService = RetrofitClient.getApiService();
    }



    // --- Lấy danh sách chờ ---
    public void fetchPendingList(int classId) {
        isLoading.postValue(true);
        runRealApiLogicForList(classId);
    }

    private void runMockLogicForList(int classId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ArrayList<StudentClass> mockList = new ArrayList<>();
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

    private void processApproval(int studentClassId, String status) {
        isApprovalLoading.postValue(true);
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

    private void processApproval(int studentId, int classId , String status) {
        isApprovalLoading.postValue(true);
        apiService.kickStudent(classId, studentId, status).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                isApprovalLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    approvalSuccessEvent.postValue(response.body().getMessage());
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

    public void approveStudent(int studentClassId) {
        processApproval(studentClassId, "APPROVED");
    }

    public void rejectStudent(int studentClassId) {
        processApproval(studentClassId, "REJECTED");
    }

    public void rejectStudent(int studentId, int classId) {
        processApproval(studentId, classId, "REJECTED");
    }

    public void approveAllPending(int classId) {
        isBulkLoading.postValue(true);
        runRealApiLogicForUpdateAll(classId, "APPROVED");
    }

    public void rejectAllPending(int classId) {
        isBulkLoading.postValue(true);
        runRealApiLogicForUpdateAll(classId, "REJECTED");
    }

    private void runRealApiLogicForUpdateAll(int classId, String status) {
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

    public void createClass(String className, String classTime) {
        isCreatingClass.postValue(true);
        runRealApiLogicForCreateClass(className, classTime);
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
        runRealApiLogicForUpdateClass(classId, className, classTime);
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

    public void fetchStudentList(int classId) {
        isStudentListLoading.postValue(true);
        runRealApiLogicForStudentList(classId);
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

    public void addGrade(Long studentId, Integer classId, String gradeType, Double score) {
        isAddingGrade.postValue(true);
        GradeRequest request = new GradeRequest(studentId, classId, gradeType, score);
        runRealApiLogicForAddGrade(request);
    }

    private void runRealApiLogicForAddGrade(GradeRequest request) {
        apiService.addGrade(request).enqueue(new Callback<Grade>() {
            @Override
            public void onResponse(Call<Grade> call, Response<Grade> response) {
                isAddingGrade.postValue(false);
                if (response.isSuccessful()) {
                    addGradeSuccessEvent.postValue(response.body());
                } else {
                    addGradeErrorEvent.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Grade> call, Throwable t) {
                isAddingGrade.postValue(false);
                addGradeErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public void updateGrade(int gradeId, Long studentId, Integer classId, String gradeType, Double score) {
        isUpdatingGrade.postValue(true);
        GradeRequest request = new GradeRequest(studentId, classId, gradeType, score);
        runRealApiLogicForUpdateGrade(gradeId, request);
    }

    private void runRealApiLogicForUpdateGrade(int gradeId, GradeRequest request) {
        apiService.updateGrade(gradeId, request).enqueue(new Callback<Grade>() {
            @Override
            public void onResponse(Call<Grade> call, Response<Grade> response) {
                isUpdatingGrade.postValue(false);
                if (response.isSuccessful()) {
                    updateGradeSuccessEvent.postValue(response.body());
                } else {
                    updateGradeErrorEvent.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Grade> call, Throwable t) {
                isUpdatingGrade.postValue(false);
                updateGradeErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public void deleteGrade(int gradeId) {
        isDeletingGrade.postValue(true);
        runRealApiLogicForDeleteGrade(gradeId);
    }

    private void runRealApiLogicForDeleteGrade(int gradeId) {
        apiService.deleteGrade(gradeId).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                isDeletingGrade.postValue(false);
                if (response.isSuccessful()) {
                    deleteGradeSuccessEvent.postValue(response.body());
                } else {
                    deleteGradeErrorEvent.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                isDeletingGrade.postValue(false);
                deleteGradeErrorEvent.postValue("Lỗi mạng: " + t.getMessage());
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

    public LiveData<List<StudentResponse>> getStudentList() { return studentListLiveData; }
    public LiveData<Boolean> getIsStudentListLoading() { return isStudentListLoading; }
    public LiveData<String> getStudentListError() { return studentListError; }

    public LiveData<Grade> getAddGradeSuccessEvent() { return addGradeSuccessEvent; }
    public LiveData<String> getAddGradeErrorEvent() { return addGradeErrorEvent; }
    public LiveData<Boolean> getIsAddingGrade() { return isAddingGrade; }

    public LiveData<Grade> getUpdateGradeSuccessEvent() { return updateGradeSuccessEvent; }
    public LiveData<String> getUpdateGradeErrorEvent() { return updateGradeErrorEvent; }
    public LiveData<Boolean> getIsUpdatingGrade() { return isUpdatingGrade; }

    public LiveData<MessageResponse> getDeleteGradeSuccessEvent() { return deleteGradeSuccessEvent; }
    public LiveData<String> getDeleteGradeErrorEvent() { return deleteGradeErrorEvent; }
    public LiveData<Boolean> getIsDeletingGrade() { return isDeletingGrade; }
}
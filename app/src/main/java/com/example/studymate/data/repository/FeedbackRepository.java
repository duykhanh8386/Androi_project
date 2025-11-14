package com.example.studymate.data.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.constants.RoleConstant;
import com.example.studymate.data.model.Feedback;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.User;
import com.example.studymate.data.model.request.FeedbackRequest;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackRepository {

    private ApiService apiService;
    private final boolean IS_MOCK_MODE = true;

    // LiveData cho danh sách feedback
    private MutableLiveData<List<Feedback>> feedbackListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    // LiveData cho sự kiện GỬI
    private MutableLiveData<Feedback> sendSuccessLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSending = new MutableLiveData<>();
    private MutableLiveData<String> sendError = new MutableLiveData<>();
    public FeedbackRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // ⭐️ HÀM MỚI:
    public void fetchFeedbackThread(int classId) {
        isLoading.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForFeedback(classId);
        } else {
            runRealApiLogicForFeedback(classId);
        }
    }

    public void sendFeedback(int classId, String content) {
        isSending.postValue(true);
        if (IS_MOCK_MODE) {
            runMockLogicForSend(classId, content);
        } else {
            runRealApiLogicForSend(classId, content);
        }
    }

    // ⭐️ HÀM MỚI: (Mock logic)
    private void runMockLogicForFeedback(int classId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ArrayList<Feedback> mockList = new ArrayList<>();
            // (Dùng POJO Feedback.java [cite: 357] - (id, content, date, isRead, classId, senderId))
            StudyClass studyClass = new StudyClass();
            studyClass.setClassId(classId);
            User student = new User(123, "Học sinh A", "2010234343", "a@gmail.com", RoleConstant.STUDENT);
            User teacher = new User(456, "Học sinh A", "2010234343", "a@gmail.com", RoleConstant.TEACHER);

            // Giả sử ID học sinh là 123, ID giáo viên là 456
//            mockList.add(new Feedback(1, "Thưa cô, em xin phép nghỉ ốm ạ.", new Date(), "false", studyClass, student));
//            mockList.add(new Feedback(2, "Ok em, nhớ nộp bài bù nhé.", new Date(), "false", studyClass, teacher));
//            mockList.add(new Feedback(3, "Dạ vâng ạ.", new Date(), "false", studyClass, student));

            isLoading.postValue(false);
            feedbackListLiveData.postValue(mockList);
        }, 1000); // Trì hoãn 1 giây
    }

    // ⭐️ HÀM MỚI: (Real API logic)
    private void runRealApiLogicForFeedback(int classId) {
        apiService.getFeedbackThread(classId).enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful()) {
                    feedbackListLiveData.postValue(response.body());
                } else {
                    error.postValue("Lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                isLoading.postValue(false);
                error.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // ⭐️ THÊM HÀM MỚI: (Mock logic)
    private void runMockLogicForSend(int classId, String content) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Giả lập 1 tin nhắn trả về
            // (Giả sử 123 là ID học sinh - khớp với mock ID ở lượt trước)
            StudyClass studyClass = new StudyClass();
            studyClass.setClassId(classId);
            User student = new User(123, "Học sinh A", "student", "a@gmail.com", RoleConstant.STUDENT);
//            Feedback newFeedback = new Feedback(
//                    (int) (Math.random() * 1000), // ID ngẫu nhiên
//                    content,
//                    new Date(),
//                    "false",
//                    studyClass,
//                    student // Gửi từ "Bạn" (khớp mock ID)
//            );
            Feedback newFeedback = new Feedback();

            isSending.postValue(false);
            sendSuccessLiveData.postValue(newFeedback);
        }, 1000); // Trì hoãn 1 giây
    }

    // ⭐️ THÊM HÀM MỚI: (Real API logic)
    private void runRealApiLogicForSend(int classId, String content) {
        FeedbackRequest request = new FeedbackRequest(classId, content);
        apiService.sendFeedback(request).enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                isSending.postValue(false);
                if (response.isSuccessful()) {
                    sendSuccessLiveData.postValue(response.body());
                } else {
                    sendError.postValue("Lỗi: Không thể gửi (code: " + response.code() + ")");
                }
            }
            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {
                isSending.postValue(false);
                sendError.postValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    // ⭐️ THÊM GETTERS MỚI:
    public LiveData<List<Feedback>> getFeedbackList() {
        return feedbackListLiveData;
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Feedback> getSendSuccessLiveData() {
        return sendSuccessLiveData;
    }
    public LiveData<Boolean> getIsSending() {
        return isSending;
    }
    public LiveData<String> getSendError() {
        return sendError;
    }
}

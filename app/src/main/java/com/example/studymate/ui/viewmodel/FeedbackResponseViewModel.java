package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Feedback;
import com.example.studymate.data.repository.FeedbackRepository;

public class FeedbackResponseViewModel extends ViewModel {

    private FeedbackRepository repository;

    public FeedbackResponseViewModel() {
        this.repository = new FeedbackRepository();
    }

    /**
     * Fragment sẽ gọi hàm này khi người dùng nhấn "Gửi"
     * @param classId ID của lớp học
     * @param content Nội dung tin nhắn
     */
    public void sendFeedback(int classId, String content) {
        repository.sendFeedback(classId, content);
    }

    // Getters để Fragment quan sát (observe)

    /**
     * Quan sát sự kiện gửi thành công
     * @return LiveData chứa tin nhắn (Feedback) vừa được gửi
     */
    public LiveData<Feedback> getSendSuccess() {
        return repository.getSendSuccessLiveData();
    }

    /**
     * Quan sát trạng thái "đang gửi..."
     * @return LiveData (true nếu đang gửi, false nếu xong)
     */
    public LiveData<Boolean> getIsSending() {
        return repository.getIsSending();
    }

    /**
     * Quan sát nếu có lỗi khi gửi
     * @return LiveData chứa thông báo lỗi (String)
     */
    public LiveData<String> getSendError() {
        return repository.getSendError();
    }
}
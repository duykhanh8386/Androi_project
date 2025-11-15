package com.example.studymate.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.studymate.data.model.Feedback;
// ⭐️ THÊM IMPORT NÀY
import com.example.studymate.data.model.request.FeedbackRequest;
import com.example.studymate.data.repository.FeedbackRepository;

public class FeedbackResponseViewModel extends ViewModel {

    private FeedbackRepository repository;

    public FeedbackResponseViewModel() {
        this.repository = new FeedbackRepository();
    }

    /**
     * Fragment sẽ gọi hàm này khi người dùng nhấn "Gửi"
     * ⭐️ BƯỚC 1: SỬA LẠI HÀM NÀY
     * @param classId ID của lớp học
     * @param content Nội dung tin nhắn
     * @param receiverId ID của người nhận (Học sinh)
     */
    public void sendFeedback(int classId, String content, Long receiverId) {
        // ⭐️ BƯỚC 2: TẠO REQUEST OBJECT
        FeedbackRequest request = new FeedbackRequest(classId, content, receiverId);

        // ⭐️ BƯỚC 3: GỌI HÀM ĐÚNG
        repository.sendFeedback(request);
    }

    // Getters để Fragment quan sát (observe)

    /**
     * Quan sát sự kiện gửi thành công
     * @return LiveData chứa tin nhắn (Feedback) vừa được gửi
     */
    public LiveData<Feedback> getSendSuccess() {
        return repository.getSendSuccessEvent();
    }

    /**
     * Quan sát trạng thái "đang gửi..."
     * ⭐️ BƯỚC 4: SỬA TÊN GETTER
     * @return LiveData (true nếu đang gửi, false nếu xong)
     */
    public LiveData<Boolean> getIsSending() {
        return repository.getSendLoading();
    }

    /**
     * Quan sát nếu có lỗi khi gửi
     * @return LiveData chứa thông báo lỗi (String)
     */
    public LiveData<String> getSendError() {
        return repository.getSendErrorEvent();
    }
}
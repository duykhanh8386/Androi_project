package com.example.studymate.ui.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.FeedbackResponseViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class FeedbackResponseFragment extends Fragment {

    private FeedbackResponseViewModel viewModel;
    private TextInputEditText edtReply;
    private Button btnSend;
    // (Bạn có thể thêm ProgressBar vào XML và ánh xạ ở đây nếu muốn)

    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Lấy classId (được gửi từ FeedbackThreadFragment)
        // (Điều này yêu cầu Bước 1 & 7 phải làm đúng)
        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
        } else {
            Toast.makeText(getContext(), "Lỗi: Không tìm thấy ID lớp học", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // "Thổi phồng" layout XML bạn đã cung cấp
        return inflater.inflate(R.layout.fragment_feedback_response, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FeedbackResponseViewModel.class);

        // Ánh xạ View (từ file XML của bạn)
        edtReply = view.findViewById(R.id.edtReply);
        btnSend = view.findViewById(R.id.btnSend);

        setupClickListeners();
        setupObservers();
    }

    // 2. Xử lý nút "Gửi"
    private void setupClickListeners() {
        btnSend.setOnClickListener(v -> {
            String content = edtReply.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
                return;
            }
            if (classId > 0) {
                // Gọi ViewModel
                viewModel.sendFeedback(classId, content);
            }
        });
    }

    // 3. Quan sát kết quả từ ViewModel
    private void setupObservers() {

        // Quan sát trạng thái "Đang gửi"
        viewModel.getIsSending().observe(getViewLifecycleOwner(), isSending -> {
            // Vô hiệu hóa nút và EditText khi đang gửi
            btnSend.setEnabled(!isSending);
            edtReply.setEnabled(!isSending);
            if(isSending) btnSend.setText("Đang gửi...");
            else btnSend.setText("Gửi");
        });

        // Quan sát sự kiện "Gửi thành công"
        viewModel.getSendSuccess().observe(getViewLifecycleOwner(), newFeedback -> {
            Toast.makeText(getContext(), "Gửi thành công", Toast.LENGTH_SHORT).show();

            // TODO: (Nâng cao) Gửi tin nhắn mới (newFeedback) này về Fragment danh sách (dùng SavedStateHandle)
            // để danh sách cập nhật ngay lập tức mà không cần gọi lại API.

            // Quay lại màn hình danh sách
            NavHostFragment.findNavController(this).popBackStack();
        });

        // Quan sát sự kiện "Gửi lỗi"
        viewModel.getSendError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
}
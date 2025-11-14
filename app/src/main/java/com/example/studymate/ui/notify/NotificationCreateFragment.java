package com.example.studymate.ui.notify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.NotificationViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class NotificationCreateFragment extends Fragment {

    private NotificationViewModel viewModel;
    private TextInputEditText edtTitle, edtContent;
    private Button btnSend;
    private ProgressBar progressBar;
    private ScrollView scrollViewContent;

    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Lấy classId (từ Bước 6)
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
        return inflater.inflate(R.layout.fragment_notification_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        // Ánh xạ View
        edtTitle = view.findViewById(R.id.edtTitle);
        edtContent = view.findViewById(R.id.edtContent);
        btnSend = view.findViewById(R.id.btnSend);
        progressBar = view.findViewById(R.id.progressBar);
        scrollViewContent = view.findViewById(R.id.scrollViewContent);

        setupClickListeners();
        setupObservers();
    }

    private void setupClickListeners() {
        btnSend.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String content = edtContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ Tiêu đề và Nội dung", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi ViewModel
            viewModel.performCreateNotification(classId, title, content);
        });
    }

    private void setupObservers() {
        // Quan sát trạng thái "Đang gửi..."
        viewModel.getIsCreating().observe(getViewLifecycleOwner(), isCreating -> {
            progressBar.setVisibility(isCreating ? View.VISIBLE : View.GONE);
            scrollViewContent.setAlpha(isCreating ? 0.3f : 1.0f);
            btnSend.setEnabled(!isCreating);
        });

        // Quan sát "Gửi thành công"
        viewModel.getCreateSuccessEvent().observe(getViewLifecycleOwner(), createdNotification -> {
            Toast.makeText(getContext(), "Gửi thông báo thành công!", Toast.LENGTH_SHORT).show();
            // Tự động quay lại màn hình Danh sách
            NavHostFragment.findNavController(this).popBackStack();
        });

        // Quan sát "Gửi thất bại"
        viewModel.getCreateErrorEvent().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
}
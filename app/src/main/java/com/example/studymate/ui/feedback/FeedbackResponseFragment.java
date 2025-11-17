package com.example.studymate.ui.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.FeedbackResponseViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class FeedbackResponseFragment extends Fragment {

    private FeedbackResponseViewModel viewModel;
    private TextInputEditText edtReply;
    private Button btnSend;
    private TextView tvReplyingTo;
    private NavController navController;

    private int classId;
    private Long receiverId;
    private String receiverName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
            receiverId = getArguments().getLong("receiverId");
            receiverName = getArguments().getString("receiverName");
        }

        if (classId == 0 || receiverId == 0L) {
            Toast.makeText(getContext(), "Lỗi: Không tìm thấy ID", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback_response, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FeedbackResponseViewModel.class);
        navController = NavHostFragment.findNavController(this);

        edtReply = view.findViewById(R.id.edtReply);
        btnSend = view.findViewById(R.id.btnSend);
        tvReplyingTo = view.findViewById(R.id.tvReplyingTo);

        if (tvReplyingTo != null && receiverName != null) {
            tvReplyingTo.setText("Trả lời cho: " + receiverName);
        }

        setupClickListeners();
        setupObservers();
    }

    private void setupClickListeners() {
        btnSend.setOnClickListener(v -> {
            String content = edtReply.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
                return;
            }
            if (classId > 0) {
                viewModel.sendFeedback(classId, content, receiverId);
            }
        });
    }

    private void setupObservers() {
        viewModel.getIsSending().observe(getViewLifecycleOwner(), isSending -> {
            btnSend.setEnabled(!isSending);
            edtReply.setEnabled(!isSending);
            if(isSending) btnSend.setText("Đang gửi...");
            else btnSend.setText("Gửi");
        });

        viewModel.getSendSuccess().observe(getViewLifecycleOwner(), newFeedback -> {
            Toast.makeText(getContext(), "Gửi thành công", Toast.LENGTH_SHORT).show();

            navController.getPreviousBackStackEntry()
                    .getSavedStateHandle()
                    .set(FeedbackThreadFragment.KEY_REFRESH_THREAD, true);
            navController.popBackStack();
        });

        viewModel.getSendError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
}
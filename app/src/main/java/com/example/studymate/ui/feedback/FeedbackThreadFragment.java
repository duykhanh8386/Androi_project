package com.example.studymate.ui.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.ui.feedback.adapter.FeedbackListAdapter;
import com.example.studymate.ui.viewmodel.FeedbackViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FeedbackThreadFragment extends Fragment {

    private FeedbackViewModel viewModel;
    private RecyclerView rvFeedback;
    private FeedbackListAdapter adapter;
    private ProgressBar progressBar;

    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // LẤY ID LỚP HỌC (từ Bước 3 và 9)
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
        return inflater.inflate(R.layout.fragment_feedback_thread, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        progressBar = view.findViewById(R.id.progressBar);
        rvFeedback = view.findViewById(R.id.rvFeedback);
        FloatingActionButton fabSendFeedback = view.findViewById(R.id.fabSendFeedback);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);

        // Setup Adapter
        adapter = new FeedbackListAdapter();
        rvFeedback.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeedback.setAdapter(adapter);

        // Quan sát
        setupObservers();

        // Tải dữ liệu
        if (classId > 0) {
            viewModel.loadFeedback(classId);
        }

        // ⭐️ SỬA LẠI HÀM CLICK NÀY
        fabSendFeedback.setOnClickListener(v -> {

            // 1. Tạo Bundle để chứa ID
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId); // "classId" phải khớp argument (Bước 1)

            // 2. Điều hướng (dùng action từ nav_graph)
            // (ID action_detail_to_feedback là action bên trong feedbackThreadFragment)
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_detail_to_feedback, bundle);
        });
    }

    private void setupObservers() {
        // Quan sát Loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvFeedback.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Quan sát Dữ liệu
        viewModel.getFeedbackList().observe(getViewLifecycleOwner(), feedbackList -> {
            adapter.submitList(feedbackList);
            // Tự động cuộn xuống tin nhắn mới nhất
            if (feedbackList.size() > 0) {
                rvFeedback.smoothScrollToPosition(feedbackList.size() - 1);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }


}
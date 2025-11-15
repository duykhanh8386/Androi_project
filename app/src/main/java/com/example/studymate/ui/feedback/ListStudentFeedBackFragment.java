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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.Feedback;
import com.example.studymate.ui.feedback.adapter.FeedbackListAdapter;
// (Import ViewModel bạn đã tạo - File 1)
import com.example.studymate.ui.viewmodel.teacher.FeedbackListViewModel;

// ⭐️ BƯỚC 1: Implement interface của Adapter (File 2)
public class ListStudentFeedBackFragment extends Fragment implements FeedbackListAdapter.OnFeedbackClickListener {

    private FeedbackListViewModel viewModel;
    private RecyclerView rvFeedbackList;
    private FeedbackListAdapter adapter;
    private ProgressBar progressBar;
    private NavController navController;

    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ⭐️ BƯỚC 2: Lấy classId (từ nav_graph)
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
        // (Sử dụng layout ĐÚNG)
        return inflater.inflate(R.layout.fragment_list_student_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ⭐️ BƯỚC 3: Ánh xạ View (dùng đúng ID)
        progressBar = view.findViewById(R.id.progressBar);
        rvFeedbackList = view.findViewById(R.id.rvFeedbackList);

        // (Khởi tạo ViewModel (File 1))
        viewModel = new ViewModelProvider(this).get(FeedbackListViewModel.class);
        navController = NavHostFragment.findNavController(this);

        // ⭐️ BƯỚC 4: Setup Adapter (File 2)
        adapter = new FeedbackListAdapter();
        adapter.setOnFeedbackClickListener(this); // Set listener là Fragment này
        rvFeedbackList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeedbackList.setAdapter(adapter);

        setupObservers();

        // Tải dữ liệu
        if (classId > 0) {
            viewModel.loadFeedbackList(classId);
        }
    }

    // ⭐️ BƯỚC 5: Setup Observers
    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvFeedbackList.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        viewModel.getFeedbackList().observe(getViewLifecycleOwner(), feedbackList -> {
            adapter.submitList(feedbackList);
            if(feedbackList == null || feedbackList.isEmpty()) {
                Toast.makeText(getContext(), "Chưa có phản hồi nào", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    // ⭐️ BƯỚC 6: Xử lý Click (Khi bấm vào CardView)
    @Override
    public void onConversationClick(Feedback feedback) {
        // (Bấm vào 1 học sinh)

        // 1. Tạo Bundle
        Bundle bundle = new Bundle();
        bundle.putInt("classId", classId);
        // (Gửi ID của học sinh (người gửi) làm ID người nhận)
        bundle.putLong("receiverId", feedback.getSenderId());
        bundle.putString("receiverName", feedback.getSenderName());

        // 2. Điều hướng đến Màn hình B (dùng action từ nav_graph)
        navController.navigate(R.id.action_detail_to_list_student_feedback, bundle);
    }
}
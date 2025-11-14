package com.example.studymate.ui.notify;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // ⭐️ MỚI
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController; // ⭐️ MỚI
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.ui.notify.adapter.NotificationListAdapter;
import com.example.studymate.ui.viewmodel.NotificationListViewModel;

// ⭐️ Đây là phiên bản cho GIÁO VIÊN (dựa trên layout ..._teacher.xml)
public class NotificationListFragment extends Fragment {

    private NotificationListViewModel viewModel;
    private RecyclerView rvNotifications;
    private NotificationListAdapter adapter;
    private ProgressBar progressBar;
    private Button btnCreateNotification; // ⭐️ MỚI
    private NavController navController; // ⭐️ MỚI

    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lấy classId (giả sử được truyền từ teacherClassDetailFragment)
        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
        }
        if (classId <= 0) {
            Toast.makeText(getContext(), "Lỗi: Không tìm thấy ID lớp học", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Dùng layout của GIÁO VIÊN
        return inflater.inflate(R.layout.fragment_notification_list_teacher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        // ⭐️ Giả sử layout _teacher có ProgressBar (nếu không, thêm vào)
        progressBar = view.findViewById(R.id.progressBar);
        rvNotifications = view.findViewById(R.id.recyclerViewNotifications);
        btnCreateNotification = view.findViewById(R.id.buttonCreateNotification); // ⭐️ MỚI
        navController = NavHostFragment.findNavController(this); // ⭐️ MỚI

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(NotificationListViewModel.class);

        // Setup Adapter (Adapter này đã xử lý click item)
        adapter = new NotificationListAdapter();
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotifications.setAdapter(adapter);

        // Quan sát
        setupObservers();

        // Tải dữ liệu
        viewModel.loadNotificationList(classId);

        // ⭐️ Xử lý click nút TẠO MỚI (theo mẫu FeedbackThreadFragment)
        btnCreateNotification.setOnClickListener(v -> {
            // 1. Tạo Bundle để chứa classId
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);

            // 2. Điều hướng (dùng action từ nav_graph.xml)
            navController.navigate(R.id.action_list_to_createNotify, bundle);
        });
    }

    private void setupObservers() {
        // Quan sát Loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (progressBar != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
            rvNotifications.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Quan sát Dữ liệu
        viewModel.getNotificationList().observe(getViewLifecycleOwner(), notifications -> {
            adapter.submitList(notifications);
        });

        // Quan sát Lỗi
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
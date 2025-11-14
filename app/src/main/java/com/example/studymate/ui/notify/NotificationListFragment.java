package com.example.studymate.ui.notify;

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
// ⭐️ THÊM IMPORT NÀY
import com.example.studymate.data.network.SessionManager;
import com.example.studymate.ui.notify.adapter.NotificationListAdapter;
import com.example.studymate.ui.viewmodel.NotificationViewModel;
// ⭐️ THÊM IMPORT NÀY
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotificationListFragment extends Fragment {

    private NotificationViewModel viewModel;
    private RecyclerView rvNotifications;
    private NotificationListAdapter adapter;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(); // ⭐️ Khởi tạo

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

        View view;
        String userRole = sessionManager.getUserRole();

        if (userRole != null && userRole.equals("ROLE_TEACHER")) {
            view = inflater.inflate(R.layout.fragment_notification_list_teacher, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_notification_list_student, container, false);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        // (ID của RecyclerView phải là 'rvNotifications' trong CẢ 2 file layout)
        progressBar = view.findViewById(R.id.progressBar);
        rvNotifications = view.findViewById(R.id.rvNotifications);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        // Setup Adapter
        adapter = new NotificationListAdapter();
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotifications.setAdapter(adapter);

        // ⭐️ BƯỚC 2: THÊM LOGIC XỬ LÝ NÚT "+" (FAB)
        // (Chỉ layout teacher mới có nút này)
        FloatingActionButton fabCreate = view.findViewById(R.id.fabCreateNotify);
        if (fabCreate != null) {
            // (Nếu fabCreate tồn tại -> Đây là Giáo viên)
            fabCreate.setOnClickListener(v -> {
                // Tạo Bundle để gửi classId
                Bundle bundle = new Bundle();
                bundle.putInt("classId", classId);

                // Điều hướng (dùng action từ nav_graph)
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_list_to_createNotify, bundle);
            });
        }

        // Quan sát
        setupObservers();

        // Tải dữ liệu
        if (classId > 0) {
            viewModel.loadNotifications(classId);
        }
    }

    private void setupObservers() {
        // Quan sát Loading (Code này của bạn đã đúng)
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvNotifications.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Quan sát Dữ liệu (Code này của bạn đã đúng)
        viewModel.getNotificationList().observe(getViewLifecycleOwner(), notifications -> {
            adapter.submitList(notifications);
            if (notifications == null || notifications.isEmpty()) {
                Toast.makeText(getContext(), "Chưa có thông báo nào", Toast.LENGTH_SHORT).show();
            }
        });

        // (Nên thêm observer cho lỗi)
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
}
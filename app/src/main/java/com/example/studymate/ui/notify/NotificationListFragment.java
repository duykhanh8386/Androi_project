package com.example.studymate.ui.notify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.studymate.constants.RoleConstant;
import com.example.studymate.data.network.SessionManager;
import com.example.studymate.ui.notify.adapter.NotificationListAdapter;
import com.example.studymate.ui.viewmodel.NotificationListViewModel;

public class NotificationListFragment extends Fragment {

    private SessionManager sessionManager;

    private Button btnCreateNotification;
    private NotificationListViewModel viewModel;
    private RecyclerView rvNotifications;
    private NotificationListAdapter adapter;
    private ProgressBar progressBar;

    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager();

        // LẤY ID LỚP HỌC (từ Bước 3 và Bước 9)
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

        String roleName = sessionManager.getUserRole();
        if (RoleConstant.TEACHER.equals(roleName)) {
            return inflater.inflate(R.layout.fragment_notification_list_teacher, container, false);
        }

        return inflater.inflate(R.layout.fragment_notification_list_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        progressBar = view.findViewById(R.id.progressBar);
        rvNotifications = view.findViewById(R.id.recyclerViewNotifications);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(NotificationListViewModel.class);

        // Setup Adapter
        adapter = new NotificationListAdapter();
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotifications.setAdapter(adapter);

        // Quan sát
        setupObservers();

        // Tải dữ liệu
        if (classId > 0) {
            viewModel.loadNotificationList(classId);
        }

        if (RoleConstant.TEACHER.equals(sessionManager.getUserRole())) {
            btnCreateNotification = view.findViewById(R.id.buttonCreateNotification);
            btnCreateNotification.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt("classId", classId);

                // TODO: Điều hướng đến tạo thông báo
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_list_to_createNotify, bundle);
            });
        };
    }

    private void setupObservers() {
        // Quan sát Loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvNotifications.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Quan sát Dữ liệu
        viewModel.getNotificationList().observe(getViewLifecycleOwner(), notifications -> {
            adapter.submitList(notifications);
        });
    }
}
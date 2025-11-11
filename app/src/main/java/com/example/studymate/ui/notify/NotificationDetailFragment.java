package com.example.studymate.ui.notify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studymate.R;
import com.example.studymate.data.model.Notification;
import com.example.studymate.ui.viewmodel.NotificationDetailViewModel;

public class NotificationDetailFragment extends Fragment {

    private NotificationDetailViewModel viewModel;
    private TextView txtTitle, txtTime, txtContent;
    private ProgressBar progressBar;
    private ScrollView scrollContent;

    private int notificationId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // LẤY ID THÔNG BÁO (từ Bước 2 và 7)
        if (getArguments() != null) {
            notificationId = getArguments().getInt("notificationId");
        } else {
            Toast.makeText(getContext(), "Lỗi: Không tìm thấy ID thông báo", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        txtTitle = view.findViewById(R.id.txtTitle);
        txtTime = view.findViewById(R.id.txtTime);
        txtContent = view.findViewById(R.id.txtContent);
        progressBar = view.findViewById(R.id.progressBar);
        scrollContent = view.findViewById(R.id.scrollContent);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(NotificationDetailViewModel.class);

        // Quan sát
        setupObservers();

        // Tải dữ liệu
        if (notificationId > 0) {
            viewModel.loadNotificationDetail(notificationId);
        }
    }

    private void setupObservers() {
        // Quan sát Loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            scrollContent.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Quan sát Dữ liệu
        viewModel.getNotificationDetail().observe(getViewLifecycleOwner(), notification -> {
            if (notification != null) {
                txtTitle.setText(notification.getNotificationTitle());
                txtTime.setText("Ngày đăng: " + notification.getCreatedAt());
                txtContent.setText(notification.getNotificationContent());
            }
        });
    }
}
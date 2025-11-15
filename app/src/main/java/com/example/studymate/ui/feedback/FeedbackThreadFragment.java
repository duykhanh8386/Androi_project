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
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController; // ⭐️ THÊM
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
// ⭐️ THÊM:
import com.example.studymate.data.network.SessionManager;
import com.example.studymate.ui.feedback.adapter.FeedbackThreadAdapter;
import com.example.studymate.ui.viewmodel.FeedbackViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FeedbackThreadFragment extends Fragment {

    public static final String KEY_REFRESH_THREAD = "refresh_feedback_thread_key";
    private FeedbackViewModel viewModel;
    private RecyclerView rvFeedback;
    private FeedbackThreadAdapter adapter;
    private ProgressBar progressBar;
    private NavController navController; // ⭐️ THÊM
    private SessionManager sessionManager; // ⭐️ THÊM

    private int classId;
    private long receiverId; // ⭐️ THÊM (ID của Học sinh)
    private String receiverName; // ⭐️ THÊM (Tên của Học sinh)

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(); // ⭐️ Khởi tạo

        // ⭐️ BƯỚC 1: LẤY TẤT CẢ DỮ LIỆU
        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
            // (Lấy ID và Tên của Học sinh từ Màn hình A)
            receiverId = getArguments().getLong("receiverId");
            receiverName = getArguments().getString("receiverName");
        } else {
            Toast.makeText(getContext(), "Lỗi: Không tìm thấy ID", Toast.LENGTH_SHORT).show();
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
        navController = NavHostFragment.findNavController(this); // ⭐️ Khởi tạo

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);

        // ⭐️ BƯỚC 2: SỬA LẠI CONSTRUCTOR CỦA ADAPTER
        // (Lấy ID của người đang đăng nhập - Giáo viên)
        Long currentUserId = sessionManager.getUserId();
        adapter = new FeedbackThreadAdapter(currentUserId);

        rvFeedback.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeedback.setAdapter(adapter);

        // Quan sát
        setupObservers();

        listenForRefreshSignal();

        // ⭐️ BƯỚC 3: SỬA LẠI HÀM TẢI DỮ LIỆU
        if (classId > 0) {
            // (Truyền ID của học sinh (receiverId) vào)
            viewModel.loadFeedbackThread(classId, receiverId);
        }

        // ⭐️ BƯỚC 4: SỬA LẠI HÀM CLICK FAB
        fabSendFeedback.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);

            // ⭐️ Gửi ID của học sinh (người nhận)
            bundle.putLong("receiverId", receiverId);
            bundle.putString("receiverName", receiverName);

            // Điều hướng (dùng action từ nav_graph)
            navController.navigate(R.id.action_detail_to_feedback, bundle);
        });
    }

    // ⭐️ BƯỚC 3: THÊM HÀM LẮNG NGHE MỚI
    private void listenForRefreshSignal() {
        // Lấy SavedStateHandle của Fragment HIỆN TẠI
        SavedStateHandle savedStateHandle = navController.getCurrentBackStackEntry()
                .getSavedStateHandle();

        // Lắng nghe LiveData với Key đã định nghĩa (và giá trị mặc định 'false')
        savedStateHandle.getLiveData(KEY_REFRESH_THREAD, false)
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean shouldRefresh) {
                        // Nếu nhận được tín hiệu "true"
                        if (shouldRefresh) {
                            Toast.makeText(getContext(), "Đang cập nhật...", Toast.LENGTH_SHORT).show();
                            // 1. Tải lại dữ liệu
                            viewModel.loadFeedbackThread(classId, receiverId);

                            // 2. Xóa tín hiệu đi (RẤT QUAN TRỌNG)
                            savedStateHandle.remove(KEY_REFRESH_THREAD);
                        }
                    }
                });
    }
    // ⭐️ BƯỚC 5: SỬA LẠI HÀM OBSERVERS
    private void setupObservers() {
        // Quan sát Loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvFeedback.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Quan sát Dữ liệu (Đổi tên hàm)
        viewModel.getFeedbackThread().observe(getViewLifecycleOwner(), feedbackList -> {
            adapter.submitList(feedbackList);
            // Tự động cuộn xuống tin nhắn mới nhất
            if (feedbackList != null && feedbackList.size() > 0) {
                rvFeedback.smoothScrollToPosition(feedbackList.size() - 1);
            }
        });

        // (Đổi tên hàm)
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
}
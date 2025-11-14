package com.example.studymate.ui.classdetail.teacher;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studymate.R;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.model.response.ClassDetailResponse;
import com.example.studymate.ui.home.HomeStudentFragment;
import com.example.studymate.ui.viewmodel.ClassDetailViewModel;
import com.example.studymate.ui.viewmodel.HomeStudentViewModel;
import com.example.studymate.ui.viewmodel.HomeTeacherViewModel;

public class ClassDetailFragment extends Fragment {

    public static final String KEY_REFRESH_DETAILS = "refresh_details_key";
    private ClassDetailViewModel viewModel;

    private HomeTeacherViewModel homeTeacherViewModel;
    private TextView tvClassNameDetail, tvClassId, tvTeacherNameDetail, tvStudentCount, tvClassTime;
    private ProgressBar progressBar;

    private Button btnStudents, btnScore, btnNotify, btnFeedback, btnAccept;
    private Button btnUpdateClass, btnDeleteClass;

    private NavController navController;
    private ScrollView scrollContent;
    private LinearLayout bottomButtons;
    private int classId;

    private ClassDetailResponse currentClassDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Nhận classId từ Bundle (được gửi từ Adapter)
        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
        } else {
            // Xử lý lỗi nếu không nhận được ID
            Toast.makeText(getContext(), "Lỗi: Không tìm thấy ID lớp học", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_detail_teacher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        tvClassNameDetail = view.findViewById(R.id.tvClassNameDetail);
        tvTeacherNameDetail = view.findViewById(R.id.tvTeacherNameDetail);
        tvClassId = view.findViewById(R.id.tvClassId);
        tvStudentCount = view.findViewById(R.id.tvStudentCount);
        tvClassTime = view.findViewById(R.id.tvClassTime);
        progressBar = view.findViewById(R.id.progressBar);
        scrollContent = view.findViewById(R.id.scrollContent);
        btnStudents = view.findViewById(R.id.btnStudents);
        btnScore = view.findViewById(R.id.btnScore);
        btnNotify = view.findViewById(R.id.btnNotify);
        btnFeedback = view.findViewById(R.id.btnFeedback);
        btnAccept = view.findViewById(R.id.btnAccept);
        btnUpdateClass = view.findViewById(R.id.btnUpdateClass);
        btnDeleteClass = view.findViewById(R.id.btnDeleteClass);

        bottomButtons = view.findViewById(R.id.bottomButtons);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(ClassDetailViewModel.class);
        homeTeacherViewModel = new ViewModelProvider(this).get(HomeTeacherViewModel.class);
        navController = NavHostFragment.findNavController(this);

        // Thiết lập observers
        setupObservers();

        // Thêm hàm setup Click Listeners
        setupClickListeners();

        listenForUpdateSignal();

        // Tải dữ liệu lần đầu
        if (currentClassDetails == null) { // Chỉ tải nếu chưa có dữ liệu
            viewModel.loadClassDetails(classId);
        }
    }

    private void listenForUpdateSignal() {
        // Lấy NavController (bạn đã có)
        NavController navController = NavHostFragment.findNavController(this);

        SavedStateHandle savedStateHandle = navController.getCurrentBackStackEntry()
                .getSavedStateHandle();

        savedStateHandle.getLiveData(KEY_REFRESH_DETAILS, false)
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean shouldRefresh) {
                        if (shouldRefresh != null && shouldRefresh) {
                            viewModel.loadClassDetails(classId);

                            // Xóa tín hiệu đi (rất quan trọng)
                            savedStateHandle.remove(KEY_REFRESH_DETAILS);
                        }
                    }
                });
    }

    private void setupObservers() {
        // Quan sát trạng thái loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                scrollContent.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                bottomButtons.setVisibility(isLoading ? View.GONE : View.VISIBLE);

                // ⭐️ Ẩn/hiện các nút dưới cùng (LinearLayout chứa btnGoBack)
                View bottomButtons = getView().findViewById(R.id.bottomButtons);
                if (bottomButtons != null) {
                    bottomButtons.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                }
            }
        });

        // Quan sát dữ liệu chi tiết
        viewModel.getClassDetail().observe(getViewLifecycleOwner(), new Observer<ClassDetailResponse>() {
            @Override
            public void onChanged(ClassDetailResponse studyClass) {
                if (studyClass != null) {

                    currentClassDetails = studyClass;

                    tvClassNameDetail.setText(studyClass.getClassName());
                    tvTeacherNameDetail.setText(studyClass.getTeacherName());
                    tvClassId.setText(studyClass.getClassJoinCode());
                    tvStudentCount.setText(String.valueOf(studyClass.getStudentCount()));
                    tvClassTime.setText(studyClass.getClassTime());
                }
            }
        });
        // (Quan sát lỗi tải chi tiết)
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + error, Toast.LENGTH_LONG).show();
            }
        });

        // ⭐️ THÊM MỚI: Quan sát sự kiện XÓA LỚP
        viewModel.getIsDeleting().observe(getViewLifecycleOwner(), isDeleting -> {
            // Vô hiệu hóa các nút khi đang xử lý
            btnDeleteClass.setEnabled(!isDeleting);
            btnUpdateClass.setEnabled(!isDeleting);
            if (isDeleting) {
                btnDeleteClass.setText("Đang xóa...");
            } else {
                btnDeleteClass.setText(R.string.btn_class_delete); // Reset text
            }
        });

        viewModel.getDeleteSuccess().observe(getViewLifecycleOwner(), successMessage -> {
            Toast.makeText(getContext(), successMessage, Toast.LENGTH_LONG).show();
            // Xóa thành công, quay về màn hình Home
            // (Pop cho đến Home, KHÔNG bao gồm Home)
            navController.popBackStack(R.id.homeTeacherFragment, false);
        });

        viewModel.getDeleteError().observe(getViewLifecycleOwner(), errorMessage -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        // Quan sát sự kiện Đăng xuất
        homeTeacherViewModel.getLogoutSuccessEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Toast.makeText(getContext(), R.string.logged_out, Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(ClassDetailFragment.this)
                            .navigate(R.id.action_teacherClassDetailFragment_to_loginFragment);
                }
            }
        });
    }

    private void setupClickListeners() {

        // Nút "Cập nhật lớp"
        btnUpdateClass.setOnClickListener(v -> {
            if (currentClassDetails != null) {
                navigateToUpdateFragment();
            } else {
                Toast.makeText(getContext(), "Đang tải dữ liệu, vui lòng thử lại...", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút "Xóa lớp"
        btnDeleteClass.setOnClickListener(v -> {
            // Hiển thị hộp thoại xác nhận
            showDeleteClassDialog();
        });

        // Nút Xem Học sinh
        btnStudents.setOnClickListener(v -> {
            // (Dùng ID action từ nav_graph của bạn)
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_detail_to_studentManage, bundle);
        });

        // Nút Xem Thông báo
        btnNotify.setOnClickListener(v -> {
            // (Dùng ID action từ nav_graph của bạn)
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_detail_to_notifications, bundle);
        });

        // Nút Hỏi đáp (Feedback)
        btnFeedback.setOnClickListener(v -> {
            // (Dùng ID action từ nav_graph của bạn)
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_detail_to_feedback, bundle);
        });

        // Nút Xem Điểm
        btnScore.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_detail_to_gradeEntry, bundle);
        });

        btnAccept.setOnClickListener(v -> {
            // 1. Tạo Bundle
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);

            // 2. Điều hướng (dùng ID action từ nav_graph)
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_detail_to_approvalList, bundle);
        });

    }

    // ⭐️ THÊM HÀM MỚI: (Để điều hướng)
    private void navigateToUpdateFragment() {
        // 1. Tạo Bundle để gửi dữ liệu qua
        Bundle bundle = new Bundle();
        bundle.putInt("classId", classId);
        bundle.putString("currentName", currentClassDetails.getClassName());
        bundle.putString("currentTime", currentClassDetails.getClassTime());

        // 2. Gọi action (từ nav_graph)
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_teacherClassDetailFragment_to_classUpdateFragment, bundle);
    }

    // (Hiển thị Dialog xác nhận Xóa)
    private void showDeleteClassDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa lớp")
                .setMessage("Bạn có chắc muốn xóa lớp học này không? Hành động này không thể hoàn tác.")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Người dùng bấm "Xóa" -> Gọi ViewModel
                    viewModel.performDeleteClass(classId);
                })
                .setNegativeButton("Hủy", null) // Bấm "Hủy" -> không làm gì
                .show();
    }

    // (onCreateOptionsMenu, onOptionsItemSelected, showLogoutDialog không đổi)
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_confirm)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        homeTeacherViewModel.performLogout();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}


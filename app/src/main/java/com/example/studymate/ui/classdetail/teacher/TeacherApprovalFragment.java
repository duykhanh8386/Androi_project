package com.example.studymate.ui.classdetail.teacher;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.example.studymate.data.model.StudentClass;
import com.example.studymate.data.model.User;
import com.example.studymate.ui.classdetail.adapter.TeacherApprovalAdapter;
import com.example.studymate.ui.viewmodel.teacher.TeacherApprovalViewModel;

public class TeacherApprovalFragment extends Fragment implements TeacherApprovalAdapter.OnApprovalClickListener {

    private TeacherApprovalViewModel viewModel;
    private RecyclerView rvApproval;
    private TeacherApprovalAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout bottomButtonLayout;
    private Button btnRejectAll, btnApproveAll;

    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Lấy classId (từ Bước 2 và 9)
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
        return inflater.inflate(R.layout.fragment_approval_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        progressBar = view.findViewById(R.id.progressBar);
        rvApproval = view.findViewById(R.id.recyclerViewApproval);
        btnRejectAll = view.findViewById(R.id.btnRejectAll);
        btnApproveAll = view.findViewById(R.id.btnApproveAll);
        bottomButtonLayout = view.findViewById(R.id.bottomButtonLayout);

        viewModel = new ViewModelProvider(this).get(TeacherApprovalViewModel.class);

        // Setup Adapter
        adapter = new TeacherApprovalAdapter();
        adapter.setOnApprovalClickListener(this); // 2. Set listener là Fragment này
        rvApproval.setLayoutManager(new LinearLayoutManager(getContext()));
        rvApproval.setAdapter(adapter);

        setupObservers();

        // Tải dữ liệu
        if (classId > 0) {
            viewModel.loadPendingList(classId);
        }

        btnApproveAll.setOnClickListener(v -> {
            showBulkConfirmDialog("Phê duyệt tất cả?",
                    "Bạn có chắc muốn phê duyệt tất cả học sinh đang chờ?",
                    () -> viewModel.approveAll(classId));
        });

        btnRejectAll.setOnClickListener(v -> {
            showBulkConfirmDialog("Từ chối tất cả?",
                    "Bạn có chắc muốn từ chối tất cả học sinh đang chờ?",
                    () -> viewModel.rejectAll(classId));
        });

        // TODO: Xử lý click cho 2 nút Approve All / Reject All
//        btnApproveAll.setOnClickListener(v -> Toast.makeText(getContext(), "Chưa implement", Toast.LENGTH_SHORT).show());
//        btnRejectAll.setOnClickListener(v -> Toast.makeText(getContext(), "Chưa implement", Toast.LENGTH_SHORT).show());
    }

    private void setupObservers() {
        // Quan sát Loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvApproval.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Quan sát Dữ liệu
        viewModel.getPendingList().observe(getViewLifecycleOwner(), pendingList -> {
            if(pendingList.isEmpty()) {
                btnApproveAll.setEnabled(false);
                btnRejectAll.setEnabled(false);
            }
            adapter.submitList(pendingList);
        });

        // Quan sát sự kiện Phê duyệt/Từ chối rieng le
        viewModel.getApprovalSuccess().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            // Tải lại danh sách sau khi thành công
            viewModel.loadPendingList(classId);
        });

        viewModel.getApprovalError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });

        // ⭐️ THÊM MỚI: Quan sát sự kiện HÀNG LOẠT
        viewModel.getIsBulkLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvApproval.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            // Vô hiệu hóa nút khi đang tải
            btnApproveAll.setEnabled(!isLoading);
            btnRejectAll.setEnabled(!isLoading);
        });

        viewModel.getBulkSuccessEvent().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            // Tải lại danh sách (sẽ thấy danh sách rỗng và tự động popBackStack)
            viewModel.loadPendingList(classId);
        });

        viewModel.getBulkErrorEvent().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    // 3. Hàm được gọi từ Adapter khi bấm nút "Check"
    @Override
    public void onApproveClick(StudentClass user) {
        // (user.getUserId() ở đây là studentClassId)
        viewModel.approveStudent(user.getStudentClassId());
    }

    // 4. Hàm được gọi từ Adapter khi bấm nút "Close"
    @Override
    public void onRejectClick(StudentClass user) {
        viewModel.rejectStudent(user.getStudentClassId());
    }

    private void showBulkConfirmDialog(String title, String message, Runnable onConfirm) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Có", (dialog, which) -> onConfirm.run())
                .setNegativeButton("Không", null)
                .show();
    }
}
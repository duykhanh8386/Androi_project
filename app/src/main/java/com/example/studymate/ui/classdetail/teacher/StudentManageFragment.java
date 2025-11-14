package com.example.studymate.ui.classdetail.teacher;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.response.StudentResponse;
// (Sửa đường dẫn import Adapter của bạn nếu cần)
import com.example.studymate.ui.classdetail.adapter.StudentManageAdapter;
import com.example.studymate.ui.viewmodel.teacher.StudentManageViewModel;

// ⭐️ Implement interface của Adapter
public class StudentManageFragment extends Fragment implements StudentManageAdapter.OnStudentKickListener {

    private StudentManageViewModel viewModel;
    private RecyclerView rvStudents;
    private StudentManageAdapter adapter;
    private ProgressBar progressBar;

    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Lấy classId (từ Bước 1)
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
        // (Sử dụng layout "fragment_student_manage.xml" như nav_graph đã định nghĩa)
        return inflater.inflate(R.layout.fragment_student_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View (từ layout fragment_student_manage.xml)
        progressBar = view.findViewById(R.id.progressBar);
        rvStudents = view.findViewById(R.id.rvStudentManage); // (ID trong layout của bạn)

        viewModel = new ViewModelProvider(this).get(StudentManageViewModel.class);

        // 2. Setup Adapter (File bạn đã cung cấp)
        adapter = new StudentManageAdapter();
        adapter.setKickListener(this); // ⭐️ Set listener là Fragment này
        rvStudents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStudents.setAdapter(adapter);

        setupObservers();

        // Tải dữ liệu
        if (classId > 0) {
            viewModel.loadStudentList(classId);
        }
    }

    private void setupObservers() {
        // Quan sát Loading (Danh sách)
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvStudents.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Quan sát Dữ liệu
        viewModel.getStudentList().observe(getViewLifecycleOwner(), studentList -> {
            adapter.submitList(studentList);
            if(studentList == null || studentList.isEmpty()) {
                Toast.makeText(getContext(), "Chưa có học sinh nào trong lớp", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });

        // ⭐️ Quan sát sự kiện "Kick" (Xóa)
        viewModel.getKickSuccess().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            // Tải lại danh sách
            viewModel.loadStudentList(classId);
        });

        viewModel.getKickError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), "Lỗi: " + error, Toast.LENGTH_SHORT).show();
        });
    }

    // ⭐️ 3. Hàm được gọi từ Adapter khi bấm nút "Thùng rác"
    @Override
    public void onKickClick(StudentResponse student) {
        // Hiển thị Dialog xác nhận
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa học sinh '" + student.getUser().getFullName() + "' ra khỏi lớp?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Gọi ViewModel
                    // (LƯU Ý: student.getUser().getUserId() PHẢI LÀ student_class_id)
                    // (Nếu không, bạn cần tìm student_class_id từ StudentResponse)

                    // TODO: Đảm bảo rằng getUserId() trả về student_class_id
                    // Nếu getUserId() trả về user_id, bạn cần thay đổi logic API
                    viewModel.kickStudent(student.getUser().getUserId());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
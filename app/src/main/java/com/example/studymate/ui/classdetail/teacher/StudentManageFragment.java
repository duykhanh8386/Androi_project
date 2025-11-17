package com.example.studymate.ui.classdetail.teacher;

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
import com.example.studymate.ui.classdetail.adapter.StudentManageAdapter;
import com.example.studymate.ui.viewmodel.teacher.StudentManageViewModel;

public class StudentManageFragment extends Fragment implements StudentManageAdapter.OnStudentKickListener {

    private StudentManageViewModel viewModel;
    private RecyclerView rvStudents;
    private StudentManageAdapter adapter;
    private ProgressBar progressBar;

    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        return inflater.inflate(R.layout.fragment_student_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        rvStudents = view.findViewById(R.id.rvStudentManage);

        viewModel = new ViewModelProvider(this).get(StudentManageViewModel.class);

        adapter = new StudentManageAdapter();
        adapter.setKickListener(this);
        rvStudents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStudents.setAdapter(adapter);
        setupObservers();
        if (classId > 0) {
            viewModel.loadStudentList(classId);
        }
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvStudents.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        viewModel.getStudentList().observe(getViewLifecycleOwner(), studentList -> {
            adapter.submitList(studentList);
            if(studentList == null || studentList.isEmpty()) {
                Toast.makeText(getContext(), "Chưa có học sinh nào trong lớp", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });

        viewModel.getKickSuccess().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            viewModel.loadStudentList(classId);
        });

        viewModel.getKickError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), "Lỗi: " + error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onKickClick(StudentResponse student) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa học sinh '" + student.getUser().getFullName() + "' ra khỏi lớp?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    viewModel.kickStudent(student.getUser().getUserId(), classId);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
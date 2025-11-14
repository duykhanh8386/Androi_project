package com.example.studymate.ui.grade.teacher;

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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.response.StudentResponse;
import com.example.studymate.ui.grade.adapter.GradeEntryAdapter;
import com.example.studymate.ui.viewmodel.teacher.StudentManageViewModel;
import com.google.gson.Gson;

// ⭐️ Implement interface của Adapter
public class GradeEntryFragment extends Fragment implements GradeEntryAdapter.OnItemClickListener {

    public static final String KEY_REFRESH_GRADES = "refresh_grades_key";
    private StudentManageViewModel viewModel;
    private RecyclerView rvGradeEntry;
    private GradeEntryAdapter adapter;
    private ProgressBar progressBar;
    private NavController navController;
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
        // (Sử dụng layout "fragment_grade_entry.xml" từ Bước 2)
        return inflater.inflate(R.layout.fragment_grade_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        progressBar = view.findViewById(R.id.progressBar);
        rvGradeEntry = view.findViewById(R.id.rvGradeEntry);

        // 2. Khởi tạo ViewModel (TÁI SỬ DỤNG)
        viewModel = new ViewModelProvider(this).get(StudentManageViewModel.class);
        navController = NavHostFragment.findNavController(this);

        // 3. Setup Adapter (File mới từ Bước 4)
        adapter = new GradeEntryAdapter();
        adapter.setOnItemClickListener(this); // ⭐️ Set listener
        rvGradeEntry.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGradeEntry.setAdapter(adapter);

        setupObservers();

        listenForRefreshSignal();

        // 4. Tải dữ liệu
        if (classId > 0) {
            viewModel.loadStudentList(classId);
        }
    }

    private void setupObservers() {
        // (Các observer này hoạt động vì chúng ta tái sử dụng ViewModel)

        // Quan sát Loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvGradeEntry.setVisibility(isLoading ? View.GONE : View.VISIBLE);
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
    }

    private void listenForRefreshSignal() {
        SavedStateHandle savedStateHandle = navController.getCurrentBackStackEntry()
                .getSavedStateHandle();

        savedStateHandle.getLiveData(KEY_REFRESH_GRADES, false)
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean shouldRefresh) {
                        if (shouldRefresh) {
                            // Tải lại danh sách
                            viewModel.loadStudentList(classId);
                            // Xóa tín hiệu
                            savedStateHandle.remove(KEY_REFRESH_GRADES);
                        }
                    }
                });
    }

    // ⭐️ 5. Hàm được gọi khi bấm vào một học sinh
    @Override
    public void onItemClick(StudentResponse student) {
        // (Bấm vào 1 học sinh)

        // 1. Chuyển List<Grade> thành JSON (String) để gửi
        String gradesJson = new Gson().toJson(student.getGrades());

        // 2. Tạo Bundle
        Bundle bundle = new Bundle();
        bundle.putInt("classId", classId);
        bundle.putLong("studentId", student.getUser().getUserId());
        bundle.putString("studentName", student.getUser().getFullName());
        bundle.putString("gradesJson", gradesJson); // ⭐️ Gửi danh sách điểm

        // 3. Điều hướng (dùng action MỚI từ nav_graph)
        navController.navigate(R.id.action_gradeEntryFragment_to_gradeDetailListFragment, bundle);
    }
}
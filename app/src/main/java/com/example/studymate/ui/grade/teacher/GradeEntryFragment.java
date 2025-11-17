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
        return inflater.inflate(R.layout.fragment_grade_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        rvGradeEntry = view.findViewById(R.id.rvGradeEntry);

        viewModel = new ViewModelProvider(this).get(StudentManageViewModel.class);
        navController = NavHostFragment.findNavController(this);

        adapter = new GradeEntryAdapter();
        adapter.setOnItemClickListener(this);
        rvGradeEntry.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGradeEntry.setAdapter(adapter);

        setupObservers();
        listenForRefreshSignal();

        if (classId > 0) {
            viewModel.loadStudentList(classId);
        }
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvGradeEntry.setVisibility(isLoading ? View.GONE : View.VISIBLE);
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
    }

    private void listenForRefreshSignal() {
        SavedStateHandle savedStateHandle = navController.getCurrentBackStackEntry()
                .getSavedStateHandle();

        savedStateHandle.getLiveData(KEY_REFRESH_GRADES, false)
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean shouldRefresh) {
                        if (shouldRefresh) {
                            viewModel.loadStudentList(classId);
                            savedStateHandle.remove(KEY_REFRESH_GRADES);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(StudentResponse student) {
        String gradesJson = new Gson().toJson(student.getGrades());

        Bundle bundle = new Bundle();
        bundle.putInt("classId", classId);
        bundle.putLong("studentId", student.getUser().getUserId());
        bundle.putString("studentName", student.getUser().getFullName());
        bundle.putString("gradesJson", gradesJson);

        navController.navigate(R.id.action_gradeEntryFragment_to_gradeDetailListFragment, bundle);
    }
}
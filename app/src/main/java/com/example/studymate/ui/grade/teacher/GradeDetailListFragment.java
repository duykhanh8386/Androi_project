package com.example.studymate.ui.grade.teacher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.Grade;
import com.example.studymate.ui.grade.adapter.GradeDetailAdapter;
import com.example.studymate.ui.viewmodel.teacher.GradeEditViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GradeDetailListFragment extends Fragment implements GradeDetailAdapter.OnGradeClickListener {

    public static final String KEY_REFRESH_GRADES = "refresh_grades_key";

    private RecyclerView rvGradeDetailList;
    private GradeDetailAdapter adapter;
    private ProgressBar progressBar;
    private NavController navController;
    private GradeEditViewModel viewModel;

    private int classId;
    private long studentId;
    private String studentName;
    private ArrayList<Grade> gradeList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
            studentId = getArguments().getLong("studentId");
            studentName = getArguments().getString("studentName");
            String gradesJson = getArguments().getString("gradesJson");

            Type listType = new TypeToken<ArrayList<Grade>>(){}.getType();
            gradeList = new Gson().fromJson(gradesJson, listType);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grade_detail_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(GradeEditViewModel.class);

        rvGradeDetailList = view.findViewById(R.id.rvGradeDetailList);
        progressBar = view.findViewById(R.id.progressBar);
        TextView tvStudentNameTitle = view.findViewById(R.id.tvStudentNameTitle);
        FloatingActionButton fabAddGrade = view.findViewById(R.id.fabAddGrade);

        tvStudentNameTitle.setText(studentName);

        adapter = new GradeDetailAdapter();
        adapter.setOnGradeClickListener(this);
        rvGradeDetailList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGradeDetailList.setAdapter(adapter);
        adapter.submitList(gradeList);

        fabAddGrade.setOnClickListener(v -> {
            navigateToAddOrEdit(false, null);
        });

        setupObservers();
        listenForRefreshSignal();
    }

    private void listenForRefreshSignal() {
        SavedStateHandle savedStateHandle = navController.getCurrentBackStackEntry()
                .getSavedStateHandle();
        savedStateHandle.getLiveData(KEY_REFRESH_GRADES, false)
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean shouldRefresh) {
                        if (shouldRefresh) {
                            navController.getPreviousBackStackEntry()
                                    .getSavedStateHandle()
                                    .set(GradeEntryFragment.KEY_REFRESH_GRADES, true);
                            Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            navController.popBackStack();
                        }
                    }
                });
    }

    private void setupObservers() {
        Observer<Boolean> loadingObserver = isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvGradeDetailList.setAlpha(isLoading ? 0.3f : 1.0f);
        };

        viewModel.getIsUpdating().observe(getViewLifecycleOwner(), loadingObserver);
        viewModel.getIsDeleting().observe(getViewLifecycleOwner(), loadingObserver);

        viewModel.getDeleteSuccess().observe(getViewLifecycleOwner(), response -> {
            Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            navController.getPreviousBackStackEntry()
                    .getSavedStateHandle()
                    .set(GradeEntryFragment.KEY_REFRESH_GRADES, true);
            navController.popBackStack();
        });

        viewModel.getDeleteError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), "Lỗi: " + error, Toast.LENGTH_SHORT).show();
        });

        viewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), updatedGrade -> {
            Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            navController.getPreviousBackStackEntry()
                    .getSavedStateHandle()
                    .set(GradeEntryFragment.KEY_REFRESH_GRADES, true);
            navController.popBackStack();
        });
    }

    @Override
    public void onEditClick(Grade grade) {
        navigateToAddOrEdit(true, grade);
    }

    @Override
    public void onDeleteClick(Grade grade) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa điểm")
                .setMessage("Bạn có chắc muốn xóa điểm " + grade.getGradeType() + ": " + grade.getScore() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    viewModel.performDeleteGrade(grade.getGradeId());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void navigateToAddOrEdit(boolean isEditMode, @Nullable Grade grade) {
        Bundle bundle = new Bundle();
        bundle.putInt("classId", classId);
        bundle.putLong("studentId", studentId);
        bundle.putString("studentName", studentName);
        bundle.putBoolean("isEditMode", isEditMode);

        if (isEditMode && grade != null) {
            bundle.putInt("gradeId", grade.getGradeId());
            bundle.putString("currentType", grade.getGradeType());
            bundle.putFloat("currentScore", Float.parseFloat(String.valueOf(grade.getScore())));
        }

        navController.navigate(R.id.action_gradeDetailListFragment_to_gradeEditDialog, bundle);
    }
}
package com.example.studymate.ui.studyclass.teacher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studymate.R;
import com.example.studymate.ui.classdetail.teacher.ClassDetailFragment;
import com.example.studymate.ui.viewmodel.teacher.ClassUpdateViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class ClassUpdateFragment extends Fragment {

    private ClassUpdateViewModel viewModel;
    private TextInputEditText edtClassName, edtClassTime;
    private Button btnUpdate;
    private ProgressBar progressBar;
    private ScrollView scrollViewContent;
    private NavController navController;

    private int classId;
    private String currentName;
    private String currentTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
            currentName = getArguments().getString("currentName");
            currentTime = getArguments().getString("currentTime");
        }
        if (classId == 0) {
            Toast.makeText(getContext(), "Lỗi: Không tìm thấy ID lớp học", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ClassUpdateViewModel.class);
        navController = NavHostFragment.findNavController(this);

        edtClassName = view.findViewById(R.id.edtClassName);
        edtClassTime = view.findViewById(R.id.edtClassTime);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        progressBar = view.findViewById(R.id.progressBar);
        scrollViewContent = view.findViewById(R.id.scrollViewContent);

        edtClassName.setText(currentName);
        edtClassTime.setText(currentTime);

        setupClickListeners();
        setupObservers();
    }

    private void setupClickListeners() {
        btnUpdate.setOnClickListener(v -> {
            String newName = edtClassName.getText().toString().trim();
            String newTime = edtClassTime.getText().toString().trim();

            if (newName.isEmpty() || newTime.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng không để trống thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.performUpdateClass(classId, newName, newTime);
        });
    }

    private void setupObservers() {
        viewModel.getIsUpdating().observe(getViewLifecycleOwner(), isUpdating -> {
            progressBar.setVisibility(isUpdating ? View.VISIBLE : View.GONE);
            scrollViewContent.setAlpha(isUpdating ? 0.3f : 1.0f);
            btnUpdate.setEnabled(!isUpdating);
        });

        viewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), updatedClass -> {
            Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

            navController.getPreviousBackStackEntry()
                    .getSavedStateHandle()
                    .set(ClassDetailFragment.KEY_REFRESH_DETAILS, true);
            navController.popBackStack();
        });

        viewModel.getUpdateError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
}
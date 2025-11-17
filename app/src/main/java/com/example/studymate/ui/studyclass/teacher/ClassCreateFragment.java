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
import androidx.navigation.fragment.NavHostFragment;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.teacher.ClassCreateViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class ClassCreateFragment extends Fragment {

    private ClassCreateViewModel viewModel;
    private TextInputEditText edtClassName, edtClassTime;
    private Button btnCreate;
    private ProgressBar progressBar;
    private ScrollView scrollViewContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ClassCreateViewModel.class);

        edtClassName = view.findViewById(R.id.edtClassName);
        edtClassTime = view.findViewById(R.id.edtClassTime);
        btnCreate = view.findViewById(R.id.btnCreate);
        progressBar = view.findViewById(R.id.progressBar);
        scrollViewContent = view.findViewById(R.id.scrollViewContent);

        setupClickListeners();
        setupObservers();
    }

    private void setupClickListeners() {
        btnCreate.setOnClickListener(v -> {
            String className = edtClassName.getText().toString().trim();
            String classTime = edtClassTime.getText().toString().trim();

            if (className.isEmpty()) {
                edtClassName.setError("Tên lớp không được để trống");
                return;
            }
            if (classTime.isEmpty()) {
                edtClassTime.setError("Thời gian không được để trống");
                return;
            }

            viewModel.performCreateClass(className, classTime);
        });
    }

    private void setupObservers() {
        viewModel.getIsCreating().observe(getViewLifecycleOwner(), isCreating -> {
            if (isCreating) {
                progressBar.setVisibility(View.VISIBLE);
                scrollViewContent.setAlpha(0.3f);
                btnCreate.setEnabled(false);
            } else {
                progressBar.setVisibility(View.GONE);
                scrollViewContent.setAlpha(1.0f);
                btnCreate.setEnabled(true);
            }
        });

        viewModel.getCreateSuccess().observe(getViewLifecycleOwner(), createdClass -> {
            Toast.makeText(getContext(), "Tạo lớp '" + createdClass.getClassName() + "' thành công!", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
        });

        viewModel.getCreateError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
}
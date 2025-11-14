package com.example.studymate.ui.studyclass.student;

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
import com.example.studymate.ui.viewmodel.student.JoinClassViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class JoinClassFragment extends Fragment {

    private JoinClassViewModel viewModel;
    private TextInputEditText edtClassCode;
    private Button btnSubmitJoin;
    private ProgressBar progressBar;
    private ScrollView scrollViewContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_join_class, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(JoinClassViewModel.class);

        // Ánh xạ View
        edtClassCode = view.findViewById(R.id.edtClassCode);
        btnSubmitJoin = view.findViewById(R.id.btnSubmitJoin);
        progressBar = view.findViewById(R.id.progressBar);
        scrollViewContent = view.findViewById(R.id.scrollViewContent);

        // Xử lý Click
        btnSubmitJoin.setOnClickListener(v -> {
            String code = edtClassCode.getText().toString().trim();
            if (code.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập mã lớp", Toast.LENGTH_SHORT).show();
                return;
            }
            // Gọi ViewModel
            viewModel.performJoinClass(code);
        });

        // Quan sát LiveData
        setupObservers();
    }

    private void setupObservers() {
        // Quan sát Loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            showLoading(isLoading);
        });

        // Quan sát Thành công
        viewModel.getJoinSuccessEvent().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            // Đóng Fragment này và quay lại Home
            NavHostFragment.findNavController(this).popBackStack();
        });

        // Quan sát Lỗi
        viewModel.getJoinErrorEvent().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            scrollViewContent.setAlpha(0.3f);
            btnSubmitJoin.setEnabled(false);
            edtClassCode.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            scrollViewContent.setAlpha(1.0f);
            btnSubmitJoin.setEnabled(true);
            edtClassCode.setEnabled(true);
        }
    }
}

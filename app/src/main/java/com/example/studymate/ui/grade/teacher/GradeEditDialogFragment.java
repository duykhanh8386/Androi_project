package com.example.studymate.ui.grade.teacher;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studymate.R;
import com.example.studymate.data.model.Grade;
import com.example.studymate.ui.viewmodel.teacher.GradeEditViewModel;
import com.google.android.material.textfield.TextInputEditText;


public class GradeEditDialogFragment extends DialogFragment {

    private GradeEditViewModel viewModel;
    private NavController navController;

    private TextView tvDialogTitle, tvStudentNameLabel;
    private Spinner spinnerGradeType;
    private TextInputEditText edtScore;
    private Button btnCancel, btnSave;

    // (Dữ liệu nhận được)
    private int classId, gradeId;
    private long studentId;
    private String studentName, currentType;
    private float currentScore;
    private boolean isEditMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Lấy dữ liệu (arguments) từ Bước 3 (nav_graph)
        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
            studentId = getArguments().getLong("studentId");
            studentName = getArguments().getString("studentName");

            // (Dữ liệu cho chế độ Sửa)
            isEditMode = getArguments().getBoolean("isEditMode");
            gradeId = getArguments().getInt("gradeId");
            currentType = getArguments().getString("currentType");
            currentScore = getArguments().getFloat("currentScore");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 2. "Thổi phồng" (Inflate) layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_edit_grade, null);

        viewModel = new ViewModelProvider(this).get(GradeEditViewModel.class);
        navController = NavHostFragment.findNavController(this);

        // 3. Ánh xạ View (từ file layout Bước 1)
        tvDialogTitle = view.findViewById(R.id.tvDialogTitle);
        tvStudentNameLabel = view.findViewById(R.id.tvStudentNameLabel);
        spinnerGradeType = view.findViewById(R.id.spinnerGradeType);
        edtScore = view.findViewById(R.id.edtScore);
        btnCancel = view.findViewById(R.id.btnDialogCancel);
        btnSave = view.findViewById(R.id.btnDialogSave);

        // 4. Setup
        tvStudentNameLabel.setText("Học sinh: " + studentName);
        setupSpinner();
        setupClickListeners();
        setupObservers();

        // 5. Xây dựng Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        return builder.create();
    }

    // (Mảng loại điểm)
    private final String[] gradeTypes = {"TX", "GK", "CK"};
    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, gradeTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGradeType.setAdapter(adapter);
    }

    private void setupFieldsForMode() {
        if (isEditMode) {
            tvDialogTitle.setText("Sửa điểm");
            btnSave.setText("Cập nhật");

            // 1. Đặt điểm cũ
            edtScore.setText(String.valueOf(currentScore));

            // 2. Chọn đúng loại điểm (TX, GK, CK) trong Spinner
            if (currentType != null) {
                for (int i = 0; i < gradeTypes.length; i++) {
                    if (gradeTypes[i].equals(currentType)) {
                        spinnerGradeType.setSelection(i);
                        break;
                    }
                }
            }
        } else {
            tvDialogTitle.setText("Nhập điểm mới");
            btnSave.setText("Lưu");
        }
    }
    private void setupClickListeners() {
        btnCancel.setOnClickListener(v -> dismiss()); // Đóng Dialog

        btnSave.setOnClickListener(v -> {
            // Lấy dữ liệu
            String type = spinnerGradeType.getSelectedItem().toString();
            String scoreStr = edtScore.getText().toString();

            if (scoreStr.isEmpty()) {
                edtScore.setError("Không được để trống");
                return;
            }

            Double score = Double.parseDouble(scoreStr);

            if (isEditMode) {
                // Gọi API Sửa
                viewModel.performUpdateGrade(gradeId, studentId, classId, type, score);
            } else {
                // Gọi API Thêm
                viewModel.performAddGrade(studentId, classId, type, score);
            }
        });
    }

    private void setupObservers() {
        // 1. Quan sát trạng thái "Đang lưu..." (Thêm hoặc Sửa)
        Observer<Boolean> addingObserver = isAdding -> {
            btnSave.setEnabled(!isAdding);
            btnCancel.setEnabled(!isAdding);
            if (isAdding) btnSave.setText("Đang lưu...");
            else btnSave.setText(isEditMode ? "Cập nhật" : "Lưu");
        };
        viewModel.getIsAdding().observe(this, addingObserver);
        viewModel.getIsUpdating().observe(this, addingObserver); // (Lắng nghe cả 2)

        // 2. Quan sát "Lưu thành công" (Thêm hoặc Sửa)
        Observer<Grade> successObserver = grade -> {
            // ⭐️ GỬI TÍN HIỆU "REFRESH" VỀ FRAGMENT TRƯỚC (Màn hình B)
            navController.getPreviousBackStackEntry()
                    .getSavedStateHandle()
                    .set(GradeDetailListFragment.KEY_REFRESH_GRADES, true);
            dismiss(); // Đóng Dialog
        };
        viewModel.getAddSuccess().observe(this, successObserver);
        viewModel.getUpdateSuccess().observe(this, successObserver); // (Lắng nghe cả 2)


        // 3. Quan sát "Lưu thất bại" (Thêm hoặc Sửa)
        Observer<String> errorObserver = error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        };
        viewModel.getAddError().observe(this, errorObserver);
        viewModel.getUpdateError().observe(this, errorObserver); // (Lắng nghe cả 2)
    }
}
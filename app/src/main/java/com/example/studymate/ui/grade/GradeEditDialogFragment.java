package com.example.studymate.ui.grade;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studymate.R;
import com.example.studymate.data.local.entity.Grade;
import com.example.studymate.ui.viewmodel.GradeViewModel;
import com.google.android.material.snackbar.Snackbar;

public class GradeEditDialogFragment extends DialogFragment {

    private GradeViewModel vm;
    private Grade currentGrade; // Grade hiện tại để sửa (nếu có)

    private TextView tvDialogTitle;
    private AutoCompleteTextView autoCompleteSubject;
    // THAY ĐỔI 1: Đổi tên biến để khớp với XML và Entity
    private EditText etQuiz, etMid, etFinal;
    private Button btnCancel, btnSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_edit_grade, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);

        vm = new ViewModelProvider(requireActivity()).get(GradeViewModel.class);

        // Tìm Views (THAY ĐỔI 2: Dùng ID đã sửa)
        tvDialogTitle = v.findViewById(R.id.textViewDialogTitle);
        autoCompleteSubject = v.findViewById(R.id.autoCompleteSubject);
        etQuiz = v.findViewById(R.id.editTextQuiz);
        etMid = v.findViewById(R.id.editTextMid);
        etFinal = v.findViewById(R.id.editTextFinal);
        btnCancel = v.findViewById(R.id.buttonDialogCancel);
        btnSave = v.findViewById(R.id.buttonDialogSave);

        // THAY ĐỔI 3: Sửa logic lấy dữ liệu (Rất quan trọng)
        String studentName = null;
        String subjectName = null;

        if (getArguments() != null) {
            currentGrade = (Grade) getArguments().getSerializable("selected_grade");
            // 'Grade' không có tên, ta phải nhận tên từ Fragment trước
            studentName = getArguments().getString("studentName");
            subjectName = getArguments().getString("subjectName");
        }

        if (currentGrade != null) {
            // Chế độ "Sửa điểm"
            tvDialogTitle.setText("Sửa điểm: " + (studentName != null ? studentName : ""));
            autoCompleteSubject.setText(subjectName != null ? subjectName : "");
            autoCompleteSubject.setEnabled(false);

            // Điền điểm cũ (THAY ĐỔI 4: Dùng tên trường đúng)
            etQuiz.setText(String.valueOf(currentGrade.quiz));
            etMid.setText(String.valueOf(currentGrade.mid));
            etFinal.setText(String.valueOf(currentGrade.fin));
        } else {
            // Chế độ "Nhập điểm"
            tvDialogTitle.setText("Nhập điểm mới");
            // Bạn cần logic để load danh sách học sinh/môn học vào đây
        }

        btnSave.setOnClickListener(view -> onSaveClicked(v));
        btnCancel.setOnClickListener(view -> dismiss());
    }

    private void onSaveClicked(View v) {
        // Validate (THAY ĐỔI 5: Dùng biến đã sửa)
        if (!isValid(etQuiz) || !isValid(etMid) || !isValid(etFinal)) {
            Snackbar.make(v, "Vui lòng nhập điểm hợp lệ (0-10)!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Lấy dữ liệu (THAY ĐỔI 6: Dùng biến đã sửa)
        float quiz = Float.parseFloat(etQuiz.getText().toString());
        float mid = Float.parseFloat(etMid.getText().toString());
        float finalGrade = Float.parseFloat(etFinal.getText().toString());
        String subject = autoCompleteSubject.getText().toString(); // Cần xử lý logic lấy subjectId

        // Gọi ViewModel để lưu
        if (currentGrade != null) {
            // Cập nhật Grade cũ (THAY ĐỔI 7: Dùng tên trường đúng)
            currentGrade.quiz = quiz;
            currentGrade.mid = mid;
            currentGrade.fin = finalGrade;
           // vm.updateGrade(currentGrade); // Giả sử có hàm này
        } else {
            // Tạo Grade mới
            // Cần thêm logic lấy studentId và classId
            // vm.saveNewGrade(classId, studentId, subject, quiz, mid, finalGrade);
        }

        dismiss();
    }

    private boolean isValid(EditText e) {
        String s = e.getText().toString().trim();
        if (TextUtils.isEmpty(s)) return false;
        try {
            float v = Float.parseFloat(s);
            return v >= 0f && v <= 10f;
        } catch (Exception ex) { return false; }
    }
}
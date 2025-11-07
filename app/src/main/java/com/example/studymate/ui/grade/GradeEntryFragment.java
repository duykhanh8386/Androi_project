package com.example.studymate.ui.grade;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.GradeViewModel;
import com.example.studymate.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;

public class GradeEntryFragment extends Fragment {
    private GradeViewModel vm;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grade_entry, container, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        vm = new ViewModelProvider(requireActivity()).get(GradeViewModel.class);
        long classId = getArguments() != null ? getArguments().getLong("classId", 1L) : 1L;
        long studentId = getArguments() != null ? getArguments().getLong("studentId", 3L) : 3L;

        EditText e1 = v.findViewById(R.id.edtQuiz);
        EditText e2 = v.findViewById(R.id.edtMid);
        EditText e3 = v.findViewById(R.id.edtFinal);
        v.findViewById(R.id.btnSave).setOnClickListener(btn -> {
            if (!isValid(e1) || !isValid(e2) || !isValid(e3)) {
                Snackbar.make(v, "Vui lòng nhập điểm hợp lệ!", Snackbar.LENGTH_SHORT).show();
                return;
            }
            float q = Float.parseFloat(e1.getText().toString());
            float m = Float.parseFloat(e2.getText().toString());
            float f = Float.parseFloat(e3.getText().toString());
            vm.save(classId, studentId, q, m, f);
        });

        vm.saveResult.observe(getViewLifecycleOwner(), ok -> {
            if (ok != null && ok) Snackbar.make(v, "Lưu điểm thành công!", Snackbar.LENGTH_SHORT).show();
            else if (ok != null) Snackbar.make(v, "Lỗi kết nối!", Snackbar.LENGTH_LONG).show();
        });
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

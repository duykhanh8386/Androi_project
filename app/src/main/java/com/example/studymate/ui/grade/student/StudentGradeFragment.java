package com.example.studymate.ui.grade.student;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studymate.R;
import com.example.studymate.data.model.Grade;
import com.example.studymate.ui.viewmodel.student.StudentGradeViewModel;


import java.util.List;

public class StudentGradeFragment extends Fragment {

    private StudentGradeViewModel viewModel;
    private ProgressBar progressBar;
    private ScrollView scrollContent;
    private View bottomButton;

    // TextViews cho điểm
    private TextView tvGradeTxValue, tvGradeGkValue, tvGradeCkValue;
    private TextView tvAverageScore, tvRankValue;

    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // LẤY ID LỚP HỌC (từ Bước 2 và 7)
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
        return inflater.inflate(R.layout.fragment_grade_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        progressBar = view.findViewById(R.id.progressBar);
        scrollContent = view.findViewById(R.id.scrollContent);
        bottomButton = view.findViewById(R.id.bottomButton);

        tvGradeTxValue = view.findViewById(R.id.tvGradeTxValue);
        tvGradeGkValue = view.findViewById(R.id.tvGradeGkValue);
        tvGradeCkValue = view.findViewById(R.id.tvGradeCkValue);
        tvAverageScore = view.findViewById(R.id.tvAverageScore);
        tvRankValue = view.findViewById(R.id.tvRankValue);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(StudentGradeViewModel.class);

        // Setup Nút "Back"
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        // Quan sát
        setupObservers();

        // Tải dữ liệu
        if (classId > 0) {
            viewModel.loadGrades(classId);
        }
    }

    private void setupObservers() {
        // Quan sát Loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            scrollContent.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            bottomButton.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Quan sát Dữ liệu (⭐️ Hơi phức tạp)
        viewModel.getGradeList().observe(getViewLifecycleOwner(), gradeList -> {
            if (gradeList != null || !gradeList.isEmpty()) {
                // Xử lý logic để hiển thị điểm
                updateGradeUI(gradeList);
            }
        });

        // Quan sát lỗi
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    // ⭐️ HÀM MỚI: Xử lý và hiển thị điểm
    private void updateGradeUI(List<Grade> gradeList) {
        StringBuilder txScores = new StringBuilder();
        String gkScore = "-";
        String ckScore = "-";

        double totalTx = 0;
        int countTx = 0;
        double gk = 0, ck = 0;

        for (Grade g : gradeList) {
            if (g.getGradeType().equalsIgnoreCase("TX")) {
                txScores.append(g.getScore()).append("   ");
                totalTx += Double.parseDouble(g.getScore());
                countTx++;
            } else if (g.getGradeType().equalsIgnoreCase("GK")) {
                gkScore = String.valueOf(g.getScore());
                gk = Double.parseDouble(g.getScore());
            } else if (g.getGradeType().equalsIgnoreCase("CK")) {
                ckScore = String.valueOf(g.getScore());
                ck = Double.parseDouble(g.getScore());
            }
        }

        // Hiển thị điểm
        tvGradeTxValue.setText(txScores.length() > 0 ? txScores.toString() : "-");
        tvGradeGkValue.setText(gkScore);
        tvGradeCkValue.setText(ckScore);

        // TODO: Tính toán điểm trung bình và xếp loại
        // (Logic này nên nằm ở Backend, nhưng tạm thời có thể tính ở đây)
        if (countTx > 0 && gk > 0 && ck > 0) {
            double avgTx = totalTx / countTx;
            double finalAvg = (avgTx * 0.2) + (gk * 0.3) + (ck * 0.5); // Giả sử tỉ lệ 20-30-50

            tvAverageScore.setText(String.format("%.1f", finalAvg));
            if (finalAvg >= 8.5) tvRankValue.setText("Giỏi");
            else if (finalAvg >= 6.5) tvRankValue.setText("Khá");
            else if (finalAvg >= 5.0) tvRankValue.setText("Trung bình");
            else tvRankValue.setText("Yếu");
        }
    }
}

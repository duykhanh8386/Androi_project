package com.example.studymate.ui.grade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
// import androidx.lifecycle.ViewModelProvider; // Bỏ import
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
// Import Grade thật để dùng cho Dialog
import com.example.studymate.data.local.entity.Grade;
// import com.example.studymate.ui.viewmodel.GradeViewModel; // Bỏ import
import com.google.android.material.floatingactionbutton.FloatingActionButton;
// import com.google.android.material.snackbar.Snackbar; // Bỏ import

import java.util.ArrayList;
import java.util.List; // Import List

public class GradeEntryFragment extends Fragment {

    // THAY ĐỔI: Lớp dữ liệu giả (định nghĩa ngay trong Fragment cho tiện)
    public static class MockGradeItem {
        public long gradeId; // Giả sử
        public long studentId;
        public String studentName;
        public String subjectName;
        public float quiz;
        public float mid;
        public float fin;

        public MockGradeItem(long gradeId, long studentId, String studentName, String subjectName, float quiz, float mid, float fin) {
            this.gradeId = gradeId;
            this.studentId = studentId;
            this.studentName = studentName;
            this.subjectName = subjectName;
            this.quiz = quiz;
            this.mid = mid;
            this.fin = fin;
        }
    }

    // private GradeViewModel vm; // Bỏ ViewModel
    private RecyclerView recyclerView;
    private GradeAdapter adapter;
    private FloatingActionButton fabAddGrade;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grade_entry, container, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);

        // vm = new ViewModelProvider(requireActivity()).get(GradeViewModel.class); // Vô hiệu hóa
        // long classId = ... // Vô hiệu hóa

        recyclerView = v.findViewById(R.id.recyclerViewStudentGrades);
        fabAddGrade = v.findViewById(R.id.fabAddGrade);

        // Cài đặt RecyclerView
        setupRecyclerView();

        // Cài đặt các hành động click
        setupClickListeners(v);

        // setupObservers(v, classId); // Vô hiệu hóa
        // vm.loadGradesForClass(classId); // Vô hiệu hóa
    }

    // THAY ĐỔI: Hàm tạo dữ liệu giả
    private List<MockGradeItem> createMockData() {
        List<MockGradeItem> list = new ArrayList<>();
        list.add(new MockGradeItem(1L, 101L, "Nguyễn Văn An", "Địa lý", 9.0f, 8.5f, 8.8f));
        list.add(new MockGradeItem(2L, 102L, "Trần Thị B", "Địa lý", -1.0f, -1.0f, -1.0f)); // Dữ liệu giả cho "chưa nhập"
        list.add(new MockGradeItem(3L, 103L, "Lê Văn C", "Địa lý", 7.0f, 7.5f, 7.2f));
        list.add(new MockGradeItem(4L, 104L, "Phạm Thị D", "Địa lý", 8.0f, 9.5f, 9.0f));
        return list;
    }

    private void setupRecyclerView() {
        // THAY ĐỔI: Khởi tạo Adapter với dữ liệu giả
        List<MockGradeItem> mockList = createMockData();
        adapter = new GradeAdapter(mockList);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Lắng nghe sự kiện click từ bên trong Adapter
        adapter.setOnItemClickListener(new GradeAdapter.OnItemClickListener() {
            @Override
            public void onEnterClick(MockGradeItem gradeInfo) {
                navigateToEditDialog(gradeInfo);
            }

            @Override
            public void onEditClick(MockGradeItem gradeInfo) {
                navigateToEditDialog(gradeInfo);
            }
        });
    }

    private void setupClickListeners(View v) {
        fabAddGrade.setOnClickListener(view -> {
            navigateToEditDialog(null); // Tạo mới
        });
    }

    // private void setupObservers(View v, long classId) { ... } // Vô hiệu hóa

    /**
     * Hàm điều hướng để mở Dialog (THAY ĐỔI: Dùng MockGradeItem)
     */
    private void navigateToEditDialog(@Nullable MockGradeItem gradeInfo) {
        Bundle args = new Bundle();
        if (gradeInfo != null) {
            // "Chế" một đối tượng Grade thật từ dữ liệu giả để gửi đi
            Grade fakeGrade = new Grade();
            fakeGrade.studentId = gradeInfo.studentId;
            // fakeGrade.classId = ... (lấy từ đâu đó nếu cần)
            fakeGrade.quiz = gradeInfo.quiz;
            fakeGrade.mid = gradeInfo.mid;
            fakeGrade.fin = gradeInfo.fin;

           // args.putSerializable("selected_grade", fakeGrade);
            args.putString("studentName", gradeInfo.studentName);
            args.putString("subjectName", gradeInfo.subjectName);
        }

        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_gradeEntryFragment_to_gradeEditDialog, args);
    }
}
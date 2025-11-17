package com.example.studymate.ui.classdetail.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.ui.classdetail.adapter.StudentListAdapter;
import com.example.studymate.ui.viewmodel.student.StudentListViewModel;

public class StudentListFragment extends Fragment {

    private StudentListViewModel viewModel;
    private RecyclerView rvStudents;
    private StudentListAdapter adapter;
    private ProgressBar progressBar;

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
        return inflater.inflate(R.layout.fragment_student_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        rvStudents = view.findViewById(R.id.rvStudents);

        viewModel = new ViewModelProvider(this).get(StudentListViewModel.class);

        adapter = new StudentListAdapter();
        rvStudents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStudents.setAdapter(adapter);
        setupObservers();

        if (classId > 0) {
            viewModel.loadStudentList(classId);
        }
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvStudents.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });
        viewModel.getStudentList().observe(getViewLifecycleOwner(), students -> {
            adapter.submitList(students);
        });
    }
}
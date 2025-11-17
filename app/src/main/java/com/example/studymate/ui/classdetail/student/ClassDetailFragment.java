package com.example.studymate.ui.classdetail.student;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studymate.R;
import com.example.studymate.data.model.response.ClassDetailResponse;
import com.example.studymate.ui.viewmodel.ClassDetailViewModel;
import com.example.studymate.ui.viewmodel.HomeStudentViewModel;

public class ClassDetailFragment extends Fragment {

    private ClassDetailViewModel viewModel;

    private HomeStudentViewModel homeStudentViewModel;
    private TextView tvClassNameDetail, tvClassId, tvTeacherNameDetail, tvStudentCount, tvClassTime;
    private ProgressBar progressBar;

    private NavController navController;

    private Button btnStudents, btnScore, btnNotify, btnFeedback, btnLeaveClass, btnGoBack;
    private ScrollView scrollContent;
    private LinearLayout bottomButtons;
    private int classId;

    private ClassDetailResponse currentClassDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
        } else {
            Toast.makeText(getContext(), "Lỗi: Không tìm thấy ID lớp học", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_detail_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvClassNameDetail = view.findViewById(R.id.tvClassNameDetail);
        tvTeacherNameDetail = view.findViewById(R.id.tvTeacherNameDetail);
        tvClassId = view.findViewById(R.id.tvClassId);
        tvStudentCount = view.findViewById(R.id.tvStudentCount);
        tvClassTime = view.findViewById(R.id.tvClassTime);
        progressBar = view.findViewById(R.id.progressBar);
        scrollContent = view.findViewById(R.id.scrollContent);
        btnStudents = view.findViewById(R.id.btnStudents);
        btnScore = view.findViewById(R.id.btnScore);
        btnNotify = view.findViewById(R.id.btnNotify);
        btnFeedback = view.findViewById(R.id.btnFeedback);
        btnLeaveClass = view.findViewById(R.id.btnLeaveClass);
        btnGoBack = view.findViewById(R.id.btnGoBack);
        bottomButtons = view.findViewById(R.id.bottomButtons);
        navController = NavHostFragment.findNavController(this);

        viewModel = new ViewModelProvider(this).get(ClassDetailViewModel.class);
        homeStudentViewModel = new ViewModelProvider(this).get(HomeStudentViewModel.class);
        setupObservers();
        setupClickListeners();
        viewModel.loadClassDetails(classId);
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                scrollContent.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                bottomButtons.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                View bottomButtons = getView().findViewById(R.id.bottomButtons);
                if (bottomButtons != null) {
                    bottomButtons.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                }
            }
        });

        viewModel.getClassDetail().observe(getViewLifecycleOwner(), new Observer<ClassDetailResponse>() {
            @Override
            public void onChanged(ClassDetailResponse studyClass) {
                if (studyClass != null) {

                    currentClassDetails = studyClass;

                    tvClassNameDetail.setText(studyClass.getClassName());
                    tvTeacherNameDetail.setText(studyClass.getTeacherName());
                    tvClassId.setText(studyClass.getClassJoinCode());
                    tvStudentCount.setText(String.valueOf(studyClass.getStudentCount()));
                    tvClassTime.setText(studyClass.getClassTime());
                }
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + error, Toast.LENGTH_LONG).show();
                }
            }
        });
        viewModel.getIsLeaveLoading().observe(getViewLifecycleOwner(), isLoading -> {
            btnLeaveClass.setEnabled(!isLoading);
            btnGoBack.setEnabled(!isLoading);
            if (isLoading) {
                btnLeaveClass.setText("Đang rời...");
            } else {
                btnLeaveClass.setText(R.string.btn_out_class);
            }
        });

        viewModel.getLeaveSuccess().observe(getViewLifecycleOwner(), successMessage -> {
            Toast.makeText(getContext(), successMessage, Toast.LENGTH_LONG).show();
            NavHostFragment.findNavController(this).popBackStack(R.id.homeStudentFragment, false);
        });

        viewModel.getLeaveError().observe(getViewLifecycleOwner(), errorMessage -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        homeStudentViewModel.getLogoutSuccessEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Toast.makeText(getContext(), R.string.logged_out, Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(com.example.studymate.ui.classdetail.student.ClassDetailFragment.this)
                            .navigate(R.id.action_studentClassDetailFragment_to_loginFragment);
                }
            }
        });
    }

    private void setupClickListeners() {
        btnStudents.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_detail_to_students, bundle);
        });

        btnNotify.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_detail_to_notifications, bundle);
        });

        btnFeedback.setOnClickListener(v -> {
            if (currentClassDetails == null) {
                Toast.makeText(getContext(), "Đang tải dữ liệu, vui lòng thử lại...", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);
            bundle.putLong("receiverId", currentClassDetails.getTeacherId());
            bundle.putString("receiverName", currentClassDetails.getTeacherName());

            navController.navigate(R.id.action_detail_to_feedback, bundle);
        });

        btnScore.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);
             NavHostFragment.findNavController(this)
                     .navigate(R.id.action_detail_to_grades, bundle);
        });

        btnLeaveClass.setOnClickListener(v -> {
            showLeaveClassDialog();
        });

        btnGoBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void showLeaveClassDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.leave_class)
                .setMessage(R.string.leave_class_confirm)
                .setPositiveButton("Có", (dialog, which) -> {
                    viewModel.performLeaveClass(classId);
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_confirm)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        homeStudentViewModel.performLogout();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}

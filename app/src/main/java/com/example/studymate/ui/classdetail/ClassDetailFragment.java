package com.example.studymate.ui.classdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.ClassViewModel;
import com.example.studymate.utils.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class ClassDetailFragment extends Fragment {
    private ClassViewModel vm;
    private SessionManager session;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_detail, container, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        vm = new ViewModelProvider(requireActivity()).get(ClassViewModel.class);
        session = new SessionManager(requireContext());
        NavController nav = Navigation.findNavController(v);

        long classId = getArguments() != null ? getArguments().getLong("classId", 1L) : 1L;

        v.findViewById(R.id.chipFeedback).setOnClickListener(btn -> {
            Bundle b = new Bundle(); b.putLong("classId", classId);
            nav.navigate(R.id.action_detail_to_feedback, b);
        });
        v.findViewById(R.id.chipNotify).setOnClickListener(btn -> {
            Bundle b = new Bundle(); b.putLong("classId", classId);
            nav.navigate(R.id.action_detail_to_notifications, b);
        });
        v.findViewById(R.id.chipScore).setOnClickListener(btn -> {
            Bundle b = new Bundle(); b.putLong("classId", classId);
            nav.navigate(R.id.action_detail_to_gradeStudent, b);
        });
        v.findViewById(R.id.chipStudents).setOnClickListener(btn -> nav.navigate(R.id.action_detail_to_studentManage));

        View btnLeave = v.findViewById(R.id.btnLeaveClass);
        btnLeave.setVisibility("STUDENT".equals(session.getRole()) ? View.VISIBLE : View.GONE);
        btnLeave.setOnClickListener(b -> new MaterialAlertDialogBuilder(requireContext())
            .setMessage("Bạn có chắc muốn rời khỏi lớp học này không?")
            .setPositiveButton("Có", (d,w) -> vm.leave(classId, session.getUserId()))
            .setNegativeButton("Không", null).show());

        vm.leaveResult.observe(getViewLifecycleOwner(), ok -> {
            if (ok != null && ok) Snackbar.make(v, "Rời lớp thành công!", Snackbar.LENGTH_SHORT).show();
            else if (ok != null) Snackbar.make(v, "Lỗi kết nối! Không thể xử lý.", Snackbar.LENGTH_LONG).show();
        });
    }
}

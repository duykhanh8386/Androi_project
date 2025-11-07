package com.example.studymate.ui.notify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.NotificationViewModel;
import com.example.studymate.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;

public class NotificationCreateFragment extends Fragment {
    private NotificationViewModel vm;
    private SessionManager session;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_create, container, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        vm = new ViewModelProvider(requireActivity()).get(NotificationViewModel.class);
        session = new SessionManager(requireContext());
        long classId = getArguments() != null ? getArguments().getLong("classId", 1L) : 1L;

        EditText edtTitle = v.findViewById(R.id.edtTitle);
        EditText edtContent = v.findViewById(R.id.edtContent);
        v.findViewById(R.id.btnSend).setOnClickListener(btn -> {
            vm.create(classId, session.getUserId(), edtTitle.getText().toString(), edtContent.getText().toString());
        });

        vm.createResult.observe(getViewLifecycleOwner(), ok -> {
            if (ok == null) Snackbar.make(v, "Vui lòng nhập đầy đủ thông tin!", Snackbar.LENGTH_SHORT).show();
            else if (ok) Snackbar.make(v, "Gửi thông báo thành công!", Snackbar.LENGTH_SHORT).show();
            else Snackbar.make(v, "Lỗi kết nối! Không thể gửi thông báo.", Snackbar.LENGTH_LONG).show();
        });
    }
}

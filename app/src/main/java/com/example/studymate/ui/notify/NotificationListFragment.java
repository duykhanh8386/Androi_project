package com.example.studymate.ui.notify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // THAY ĐỔI 1: Import Button

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.NotificationViewModel;
import com.example.studymate.utils.SessionManager;
// import com.google.android.material.floatingactionbutton.FloatingActionButton; // THAY ĐỔI 2: Không cần FAB

public class NotificationListFragment extends Fragment {
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_list, container, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        NotificationViewModel vm = new ViewModelProvider(requireActivity()).get(NotificationViewModel.class);
        SessionManager session = new SessionManager(requireContext());
        long classId = getArguments() != null ? getArguments().getLong("classId", 1L) : 1L;

        // THAY ĐỔI 3: Dùng đúng ID của RecyclerView
        RecyclerView rv = v.findViewById(R.id.recyclerViewNotifications);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        // TODO: set adapter with vm.listByClass(classId)

        // THAY ĐỔI 4: Đổi FAB thành Button và dùng đúng ID
        Button buttonCreate = v.findViewById(R.id.buttonCreateNotification);
        buttonCreate.setVisibility("TEACHER".equals(session.getRole()) ? View.VISIBLE : View.GONE);
        buttonCreate.setOnClickListener(btn -> {
            Bundle b = new Bundle(); b.putLong("classId", classId);
            // Action này đúng, lấy từ nav_graph
            Navigation.findNavController(v).navigate(R.id.action_list_to_createNotify, b);
        });
    }
}
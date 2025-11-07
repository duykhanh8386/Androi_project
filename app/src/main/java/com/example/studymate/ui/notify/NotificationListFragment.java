package com.example.studymate.ui.notify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        RecyclerView rv = v.findViewById(R.id.rvNoti);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        // TODO: set adapter with vm.listByClass(classId)

        FloatingActionButton fab = v.findViewById(R.id.fabCreate);
        fab.setVisibility("TEACHER".equals(session.getRole()) ? View.VISIBLE : View.GONE);
        fab.setOnClickListener(btn -> {
            Bundle b = new Bundle(); b.putLong("classId", classId);
            Navigation.findNavController(v).navigate(R.id.action_list_to_createNotify, b);
        });
    }
}

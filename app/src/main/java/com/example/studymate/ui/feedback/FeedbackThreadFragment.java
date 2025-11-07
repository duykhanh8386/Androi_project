package com.example.studymate.ui.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.local.entity.Feedback;
import com.example.studymate.ui.viewmodel.FeedbackViewModel;
import com.example.studymate.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FeedbackThreadFragment extends Fragment {
    private FeedbackViewModel vm;
    private SessionManager session;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback_thread, container, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        vm = new ViewModelProvider(requireActivity()).get(FeedbackViewModel.class);
        session = new SessionManager(requireContext());

        long classId = getArguments() != null ? getArguments().getLong("classId", 1L) : 1L;
        vm.load(classId);

        RecyclerView rv = v.findViewById(R.id.rvMessages);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        MessageAdapter adapter = new MessageAdapter();
        rv.setAdapter(adapter);

        vm.getMessages().observe(getViewLifecycleOwner(), list -> {
            // Simple adapter demo uses dummy count; here we just notify to refresh UI placeholder
            adapter.notifyDataSetChanged();
        });
// demo
        List<MessageAdapter.MessageItem> demo = new ArrayList<>();
        demo.add(new MessageAdapter.MessageItem("Chào cô ạ", true, System.currentTimeMillis()));
        demo.add(new MessageAdapter.MessageItem("Chào em", false, System.currentTimeMillis()));
        adapter.submitList(demo);
        EditText edt = v.findViewById(R.id.edtMessage);
        v.findViewById(R.id.btnSend).setOnClickListener(btn -> {
            String content = edt.getText().toString().trim();
            vm.send(classId, session.getUserId(), content, false);
        });

        vm.sendResult.observe(getViewLifecycleOwner(), ok -> {
            if (ok == null) Snackbar.make(v, R.string.input_required, Snackbar.LENGTH_SHORT).show();
            else if (ok) { ((EditText)v.findViewById(R.id.edtMessage)).setText(""); Snackbar.make(v, R.string.send_success, Snackbar.LENGTH_SHORT).show(); }
            else Snackbar.make(v, "Lỗi kết nối! Không thể gửi phản hồi.", Snackbar.LENGTH_LONG).show();
        });
    }
}

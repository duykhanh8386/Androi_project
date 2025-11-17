package com.example.studymate.ui.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.network.SessionManager;
import com.example.studymate.ui.feedback.adapter.FeedbackThreadAdapter;
import com.example.studymate.ui.viewmodel.FeedbackViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FeedbackThreadFragment extends Fragment {

    public static final String KEY_REFRESH_THREAD = "refresh_feedback_thread_key";
    private FeedbackViewModel viewModel;
    private RecyclerView rvFeedback;
    private FeedbackThreadAdapter adapter;
    private ProgressBar progressBar;
    private NavController navController;
    private SessionManager sessionManager;

    private int classId;
    private long receiverId;
    private String receiverName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager();

        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
            receiverId = getArguments().getLong("receiverId");
            receiverName = getArguments().getString("receiverName");
        } else {
            Toast.makeText(getContext(), "Lỗi: Không tìm thấy ID", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback_thread, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        rvFeedback = view.findViewById(R.id.rvFeedback);
        FloatingActionButton fabSendFeedback = view.findViewById(R.id.fabSendFeedback);
        navController = NavHostFragment.findNavController(this);

        viewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);

        Long currentUserId = sessionManager.getUserId();
        adapter = new FeedbackThreadAdapter(currentUserId);

        rvFeedback.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeedback.setAdapter(adapter);
        setupObservers();

        listenForRefreshSignal();

        if (classId > 0) {
            viewModel.loadFeedbackThread(classId, receiverId);
        }

        fabSendFeedback.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putInt("classId", classId);

            bundle.putLong("receiverId", receiverId);
            bundle.putString("receiverName", receiverName);
            navController.navigate(R.id.action_detail_to_feedback, bundle);
        });
    }

    private void listenForRefreshSignal() {
        SavedStateHandle savedStateHandle = navController.getCurrentBackStackEntry()
                .getSavedStateHandle();

        savedStateHandle.getLiveData(KEY_REFRESH_THREAD, false)
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean shouldRefresh) {
                        if (shouldRefresh) {
                            Toast.makeText(getContext(), "Đang cập nhật...", Toast.LENGTH_SHORT).show();
                            viewModel.loadFeedbackThread(classId, receiverId);
                            savedStateHandle.remove(KEY_REFRESH_THREAD);
                        }
                    }
                });
    }
    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvFeedback.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        viewModel.getFeedbackThread().observe(getViewLifecycleOwner(), feedbackList -> {
            adapter.submitList(feedbackList);
            if (feedbackList != null && feedbackList.size() > 0) {
                rvFeedback.smoothScrollToPosition(feedbackList.size() - 1);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }
}
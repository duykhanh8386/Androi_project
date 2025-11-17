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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.Feedback;
import com.example.studymate.ui.feedback.adapter.FeedbackListAdapter;
import com.example.studymate.ui.viewmodel.teacher.FeedbackListViewModel;

public class ListStudentFeedBackFragment extends Fragment implements FeedbackListAdapter.OnFeedbackClickListener {

    private FeedbackListViewModel viewModel;
    private RecyclerView rvFeedbackList;
    private FeedbackListAdapter adapter;
    private ProgressBar progressBar;
    private NavController navController;

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
        return inflater.inflate(R.layout.fragment_list_student_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        rvFeedbackList = view.findViewById(R.id.rvFeedbackList);

        viewModel = new ViewModelProvider(this).get(FeedbackListViewModel.class);
        navController = NavHostFragment.findNavController(this);

        adapter = new FeedbackListAdapter();
        adapter.setOnFeedbackClickListener(this); // Set listener là Fragment này
        rvFeedbackList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeedbackList.setAdapter(adapter);

        setupObservers();

        if (classId > 0) {
            viewModel.loadFeedbackList(classId);
        }
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvFeedbackList.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        viewModel.getFeedbackList().observe(getViewLifecycleOwner(), feedbackList -> {
            adapter.submitList(feedbackList);
            if(feedbackList == null || feedbackList.isEmpty()) {
                Toast.makeText(getContext(), "Chưa có phản hồi nào", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onConversationClick(Feedback feedback) {
        Bundle bundle = new Bundle();
        bundle.putInt("classId", classId);
        bundle.putLong("receiverId", feedback.getSenderId());
        bundle.putString("receiverName", feedback.getSenderName());

        navController.navigate(R.id.action_detail_to_list_student_feedback, bundle);
    }
}
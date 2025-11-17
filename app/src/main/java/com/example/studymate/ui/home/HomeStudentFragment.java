package com.example.studymate.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.StudyClass;
import com.example.studymate.data.network.SessionManager;
import com.example.studymate.ui.studyclass.adapter.ClassListAdapter;
import com.example.studymate.ui.viewmodel.HomeStudentViewModel;

import java.util.List;

public class HomeStudentFragment extends Fragment {

    private SessionManager sessionManager;
    private HomeStudentViewModel viewModel;
    private RecyclerView rvStudentClasses;
    private ClassListAdapter adapter;

    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_list_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager();
        viewModel = new ViewModelProvider(this).get(HomeStudentViewModel.class);
        progressBar = view.findViewById(R.id.progressBar);
        rvStudentClasses = view.findViewById(R.id.rvStudentClasses);

        adapter = new ClassListAdapter(sessionManager);
        rvStudentClasses.setAdapter(adapter);

        rvStudentClasses.setLayoutManager(new GridLayoutManager(getContext(), 2));

        Button btnJoin = view.findViewById(R.id.btnJoinClass);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(HomeStudentFragment.this)
                        .navigate(R.id.action_home_to_joinClass);
            }
        });
        setupObservers();
        viewModel.fetchStudentClasses();
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
                        viewModel.performLogout();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void setupObservers() {
        viewModel.getClassList().observe(getViewLifecycleOwner(), new Observer<List<StudyClass>>() {
            @Override
            public void onChanged(List<StudyClass> studyClasses) {
                adapter.submitList(studyClasses);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    progressBar.setVisibility(View.VISIBLE);
                    rvStudentClasses.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    rvStudentClasses.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.getLogoutSuccessEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Toast.makeText(getContext(), R.string.logged_out, Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(HomeStudentFragment.this)
                            .navigate(R.id.action_homeStudentFragment_to_loginFragment);
                }
            }
        });
    }
}
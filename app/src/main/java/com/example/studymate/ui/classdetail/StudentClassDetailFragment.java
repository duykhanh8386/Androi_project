package com.example.studymate.ui.classdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.ClassViewModel;
import com.example.studymate.utils.SessionManager;

public class StudentClassDetailFragment extends Fragment {
    private ClassViewModel vm;
    private SessionManager session;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_detail_student, container, false);
    }


}

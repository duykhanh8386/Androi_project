package com.example.studymate.ui.classdetail.teacher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.ClassViewModel;

public class ClassDetailFragment extends Fragment {
    private ClassViewModel vm;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_detail_teacher, container, false);
    }


}

package com.example.studymate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studymate.R;

public class HomeAdminFragment extends Fragment {

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_admin, container, false);
    }
    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s){
        super.onViewCreated(v,s);
        setHasOptionsMenu(true); // dùng menu của MainActivity
    }
}

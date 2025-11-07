package com.example.studymate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.ui.home.adapter.ClassAdapter;
import com.example.studymate.ui.home.model.ClassItem;
import com.example.studymate.ui.viewmodel.ClassViewModel;

public class HomeTeacherFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_list, container, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);

        ClassViewModel vm = new ViewModelProvider(requireActivity()).get(ClassViewModel.class);
        NavController nav = Navigation.findNavController(v);

        RecyclerView rv = v.findViewById(R.id.rvClasses);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        ClassAdapter adapter = new ClassAdapter();
        adapter.setOnItemClick((ClassItem item, int pos) -> {
            Bundle b = new Bundle();
            b.putLong("classId", item.getId());              // dùng id thật từ item
            nav.navigate(R.id.action_home_to_classDetail, b);
        });
        rv.setAdapter(adapter);

        // Nạp dữ liệu từ ViewModel (đồng bộ với kiểu List<ClassItem>)
        vm.getClasses().observe(getViewLifecycleOwner(), adapter::submitList);

        // FAB -> tạo lớp (để TODO sau)
        v.findViewById(R.id.fabPrimary).setOnClickListener(btn -> {
            // TODO: mở bottom sheet tạo lớp
        });
    }
}

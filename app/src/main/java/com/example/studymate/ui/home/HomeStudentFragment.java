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
import com.example.studymate.ui.viewmodel.ClassViewModel;
import com.example.studymate.utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class HomeStudentFragment extends Fragment {
    private ClassViewModel vm;
    private SessionManager session;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_list_teacher, container, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        vm = new ViewModelProvider(requireActivity()).get(ClassViewModel.class);
        session = new SessionManager(requireContext());
        NavController nav = Navigation.findNavController(v);
        RecyclerView rv = v.findViewById(R.id.rvStudentClasses);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        ClassAdapter adapter = new ClassAdapter();
        adapter.setOnItemClick((name, pos) -> {
            Bundle args = new Bundle();
            args.putLong("classId", pos + 1L);
            nav.navigate(R.id.action_home_to_classDetail, args); // dùng đúng id trong nav_graph
        });
        rv.setAdapter(adapter);

        // nạp dữ liệu
        vm.getClasses().observe(getViewLifecycleOwner(), adapter::submitList);


//        v.findViewById(R.id.fabPrimary).setOnClickListener(btn -> {
//            BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
//            View sheet = getLayoutInflater().inflate(R.layout.bottom_sheet_join_class, null);
//            TextInputEditText edt = sheet.findViewById(R.id.edtCode);
//            MaterialButton join = sheet.findViewById(R.id.btnJoin);
//            join.setOnClickListener(b -> {
//                String code = edt.getText() != null ? edt.getText().toString() : "";
//                vm.joinByCode(code, session.getUserId());
//                dialog.dismiss();
//            });
//            dialog.setContentView(sheet); dialog.show();
//        });
    }
}

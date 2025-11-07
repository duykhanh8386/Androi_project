package com.example.studymate.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.LoginViewModel;
import com.example.studymate.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {

    private SessionManager session;
    private LoginViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        session = new SessionManager(requireContext());
        vm = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        NavController nav = Navigation.findNavController(v);

        MaterialButtonToggleGroup group = v.findViewById(R.id.tglRole);
        TextInputEditText edtUser = v.findViewById(R.id.edtUsername);
        TextInputEditText edtPass = v.findViewById(R.id.edtPassword);
        MaterialButton btnLogin = v.findViewById(R.id.btnLogin);

        // Chọn role -> lưu vào Session
        group.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup g, int checkedId, boolean isChecked) {
                if (!isChecked) return;
                String role = "STUDENT";
                if (checkedId == R.id.tabAdmin)   role = "ADMIN";
                else if (checkedId == R.id.tabTeacher) role = "TEACHER";
                else if (checkedId == R.id.tabStudent) role = "STUDENT";
                session.setRole(role);
            }
        });

        // Đăng nhập
        btnLogin.setOnClickListener(b -> {
            String u = edtUser.getText() == null ? "" : edtUser.getText().toString().trim();
            String p = edtPass.getText() == null ? "" : edtPass.getText().toString().trim();
            if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
                Snackbar.make(v, R.string.login_fill_all, Snackbar.LENGTH_SHORT).show();
                return;
            }
            String role = session.getRole();
            vm.login(u, p, role);

        });

        // Quan sát kết quả login
        vm.getLoginState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
            switch (state.status) {
                case SUCCESS: {
                    Snackbar.make(v, R.string.login_success, Snackbar.LENGTH_SHORT).show();
                    // Điều hướng theo role (ưu tiên role trong state, fallback session)
                    String role = state.role != null ? state.role : session.getRole();
                    if ("ADMIN".equals(role)) {
                        nav.navigate(R.id.action_login_to_homeAdmin);
                    } else if ("TEACHER".equals(role)) {
                        nav.navigate(R.id.action_login_to_homeTeacher);
                    } else {
                        nav.navigate(R.id.action_login_to_homeStudent);
                    }
                    break;
                }
                case WRONG_ROLE:
                    Snackbar.make(v, R.string.login_wrong_role, Snackbar.LENGTH_LONG).show();
                    break;
                case INVALID:
                    Snackbar.make(v, R.string.login_invalid, Snackbar.LENGTH_LONG).show();
                    break;
                case INACTIVE:
                    Snackbar.make(v, R.string.login_inactive, Snackbar.LENGTH_LONG).show();
                    break;
                case DISABLED:
                    Snackbar.make(v, R.string.login_disabled, Snackbar.LENGTH_LONG).show();
                    break;
                case DB_ERROR:
                    Snackbar.make(v, R.string.common_db_error, Snackbar.LENGTH_LONG).show();
                    break;
            }
        });
    }
}

package com.example.studymate.ui.auth;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studymate.R;
import com.example.studymate.constants.RoleConstant;
import com.example.studymate.data.model.response.LoginResponse;
import com.example.studymate.ui.viewmodel.LoginViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {

    private MaterialButton tabStudent, tabAdmin, tabTeacher;
    private List<MaterialButton> roleButtons = new ArrayList<>();
    private String selectedRole = RoleConstant.STUDENT; // Vai trò mặc định

    private TextInputEditText edtUsername;
    private TextInputEditText edtPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    private LoginViewModel loginViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Ánh xạ các View
        edtUsername = view.findViewById(R.id.edtUsername);
        edtPassword = view.findViewById(R.id.edtPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        progressBar = view.findViewById(R.id.progressBar);

        // === THAY ĐỔI: Ánh xạ các nút vai trò mới ===
        tabStudent = view.findViewById(R.id.tabStudent);
        tabAdmin = view.findViewById(R.id.tabAdmin);
        tabTeacher = view.findViewById(R.id.tabTeacher);

        // Thêm các nút vào danh sách để quản lý
        roleButtons.add(tabStudent);
        roleButtons.add(tabAdmin);
        roleButtons.add(tabTeacher);

        // Thiết lập trạng thái ban đầu cho các nút (Học sinh được chọn mặc định)
        updateRoleButtonStyles(tabStudent);

        // === THAY ĐỔI: Thiết lập sự kiện click cho từng nút vai trò ===
        tabStudent.setOnClickListener(v -> {
            selectedRole = RoleConstant.STUDENT;
            updateRoleButtonStyles((MaterialButton) v);
        });

        tabAdmin.setOnClickListener(v -> {
            selectedRole = RoleConstant.ADMIN;
            updateRoleButtonStyles((MaterialButton) v);
        });

        tabTeacher.setOnClickListener(v -> {
            selectedRole = RoleConstant.TEACHER;
            updateRoleButtonStyles((MaterialButton) v);
        });

        // Đăng ký sự kiện Click cho nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), R.string.input_required, Toast.LENGTH_SHORT).show();
                return;
            }

            showLoading(true);
            // Sử dụng biến selectedRole đã được cập nhật
            loginViewModel.performLogin(username, password, selectedRole);
        });

        setupObservers();
    }

    private void updateRoleButtonStyles(MaterialButton selectedButton) {
        for (MaterialButton button : roleButtons) {
            if (button == selectedButton) {
                // Nút được chọn: màu nền xanh, chữ trắng
                button.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(requireContext(), R.color.btn_green)
                ));
                button.setTextColor(Color.WHITE);
            } else {
                button.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.sm_text_muted));
            }
        }
    }


    /**
     * Quan sát (Observe) LiveData từ ViewModel
     */
    private void setupObservers() {
        loginViewModel.getLoginResponse().observe(getViewLifecycleOwner(), loginResponse -> {
            showLoading(false);
            if (loginResponse != null) {
                Toast.makeText(getContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                switch (loginResponse.getUser().getRoleName()) {
                    case "ROLE_ADMIN":
                        NavHostFragment.findNavController(LoginFragment.this)
                                .navigate(R.id.action_login_to_homeAdmin);
                        break;
                    case "ROLE_TEACHER":
                        NavHostFragment.findNavController(LoginFragment.this)
                                .navigate(R.id.action_login_to_homeTeacher);
                        break;
                    case "ROLE_STUDENT":
                        NavHostFragment.findNavController(LoginFragment.this)
                                .navigate(R.id.action_login_to_homeStudent);
                        break;
                }
            }
        });

        loginViewModel.getLoginError().observe(getViewLifecycleOwner(), errorMessage -> {
            showLoading(false);
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Hàm tiện ích để bật/tắt loading
     * === THAY ĐỔI: Cập nhật để vô hiệu hóa các nút riêng lẻ ===
     */
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!isLoading);
        edtUsername.setEnabled(!isLoading);
        edtPassword.setEnabled(!isLoading);
        // Vô hiệu hóa/Bật lại các nút vai trò
        for (MaterialButton button : roleButtons) {
            button.setEnabled(!isLoading);
        }
    }
}

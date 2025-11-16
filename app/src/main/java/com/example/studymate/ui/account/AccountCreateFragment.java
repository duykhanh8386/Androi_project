package com.example.studymate.ui.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.studymate.R;
import com.example.studymate.data.model.User;
import com.example.studymate.data.model.request.CreateUserRequest;
import com.example.studymate.data.repository.UserRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class AccountCreateFragment extends Fragment {

    private TextInputEditText edtFullname, edtUsername, edtPassword;
    private SwitchMaterial swActive;
    private MaterialButtonToggleGroup tglRole;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtFullname = view.findViewById(R.id.edtFullname);
        edtUsername = view.findViewById(R.id.edtUsername);
        edtPassword = view.findViewById(R.id.edtPassword);
        swActive    = view.findViewById(R.id.switchActive);
        tglRole     = view.findViewById(R.id.tglRole);
        MaterialButton btnCreate  = view.findViewById(R.id.btnCreate);

        if (tglRole.getCheckedButtonId() == View.NO_ID) {
            tglRole.check(R.id.btnRoleStudent);
        }

        btnCreate.setOnClickListener(this::doCreate);
    }

    private void doCreate(View v) {
        if (edtFullname == null || edtUsername == null || edtPassword == null
                || swActive == null || tglRole == null) {
            Toast.makeText(getContext(),
                    "Thiếu view trong layout. Kiểm tra id!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String fullname = text(edtFullname);
        String username = text(edtUsername);
        String password = text(edtPassword);

        if (TextUtils.isEmpty(fullname)
                || TextUtils.isEmpty(username)
                || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(),
                    "Vui lòng nhập đầy đủ thông tin!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // roleName đúng format với DB
        String roleName = (R.id.btnRoleTeacher == tglRole.getCheckedButtonId())
                ? "ROLE_TEACHER"
                : "ROLE_STUDENT";

        boolean enable = swActive.isChecked();

        CreateUserRequest req = new CreateUserRequest();
        req.fullName = fullname;
        req.username = username;
        req.password = password;
        req.roleName = roleName;
        req.enable   = enable;

        new UserRepository().create(req).observe(getViewLifecycleOwner(), (User u) -> {
            if (u == null) {
                Toast.makeText(getContext(),
                        "Tạo tài khoản thất bại (400/401/409). Kiểm tra lại dữ liệu!",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(),
                        "Tạo tài khoản thành công!",
                        Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    private String text(TextInputEditText e) {
        return e == null ? "" : String.valueOf(e.getText()).trim();
    }
}

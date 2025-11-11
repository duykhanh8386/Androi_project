// app/src/main/java/com/example/studymate/ui/account/AccountCreateFragment.java
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

    private TextInputEditText edtFullname, edtEmail, edtUsername, edtPhone, edtPassword;
    private SwitchMaterial swActive; private MaterialButtonToggleGroup tglRole;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtFullname = view.findViewById(R.id.edtFullname);
        edtEmail    = view.findViewById(R.id.edtEmail);
        edtUsername = view.findViewById(R.id.edtUsername);
        edtPhone    = view.findViewById(R.id.edtPhone);
        edtPassword = view.findViewById(R.id.edtPassword);
        swActive    = view.findViewById(R.id.switchActive);
        tglRole     = view.findViewById(R.id.tglRole);
        MaterialButton btnCreate  = view.findViewById(R.id.btnCreate);

        if (tglRole.getCheckedButtonId() == View.NO_ID) tglRole.check(R.id.btnRoleStudent);
        btnCreate.setOnClickListener(this::doCreate);
    }

    private void doCreate(View v) {
        if (edtFullname==null || edtUsername==null || edtPassword==null || swActive==null || tglRole==null) {
            Toast.makeText(getContext(), "Thiếu view trong layout. Kiểm tra id!", Toast.LENGTH_LONG).show();
            return;
        }
        String fullname = text(edtFullname), email = text(edtEmail), username = text(edtUsername),
            phone = text(edtPhone), password = text(edtPassword);
        if (TextUtils.isEmpty(fullname) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        String role = (R.id.btnRoleTeacher == tglRole.getCheckedButtonId()) ? "TEACHER" : "STUDENT";
        String status = swActive.isChecked() ? "ACTIVE" : "INACTIVE";

        CreateUserRequest req = new CreateUserRequest();
        req.fullname = fullname; req.email = email; req.username = username;
        req.phone = phone; req.password = password; req.role = role; req.status = status;

        new UserRepository().create(req).observe(getViewLifecycleOwner(), (User u) -> {
            if (u == null) {
                Toast.makeText(getContext(),
                    "Tên đăng nhập đã tồn tại hoặc lỗi kết nối (401/409).",
                    Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    private String text(TextInputEditText e){ return e==null?"" : String.valueOf(e.getText()).trim(); }
}

// app/src/main/java/com/example/studymate/ui/account/AccountListFragment.java
package com.example.studymate.ui.account;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.User;
import com.example.studymate.data.repository.UserRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class AccountListFragment extends Fragment {

    private UserRepository repo;
    private AccountAdapter adapter;
    private String role = "ALL";
    private String status = "ALL";
    private int page = 0, size = 20;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pending;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repo = new UserRepository();

        RecyclerView rv = view.findViewById(R.id.rvAccounts);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AccountAdapter(this::confirmDisable);
        rv.setAdapter(adapter);

        MaterialButton btnCreate = view.findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(v -> Navigation.findNavController(v)
            .navigate(R.id.action_acclist_to_create));

        // Ô tìm kiếm: chấp mọi layout
        View searchBox = view.findViewById(R.id.searchBar);
        wireSearchInput(searchBox);

        ChipGroup chips = view.findViewById(R.id.chips);
        Chip chipAll = view.findViewById(R.id.chipAll);
        Chip chipTeacher = view.findViewById(R.id.chipTeacher);
        Chip chipStudent = view.findViewById(R.id.chipStudent);
        Chip chipActive = view.findViewById(R.id.chipActive);
        Chip chipInactive = view.findViewById(R.id.chipInactive);

        chipAll.setOnClickListener(v -> { role = "ALL"; status = "ACTIVE"; search(""); });
        chipTeacher.setOnClickListener(v -> { role = "ROLE_TEACHER"; status = "ALL"; search(""); });
        chipStudent.setOnClickListener(v -> { role = "ROLE_STUDENT"; status = "ALL"; search(""); });
        chipActive.setOnClickListener(v -> { role = "ALL"; status = "ACTIVE"; search(""); });
        chipInactive.setOnClickListener(v -> { role = "ALL"; status = "INACTIVE"; search(""); });

        search("");
    }

    private void wireSearchInput(View searchBox) {
        if (searchBox == null) return;

        if (searchBox instanceof SearchView) {
            SearchView sv = (SearchView) searchBox;
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override public boolean onQueryTextSubmit(String q) { debounceSearch(q == null ? "" : q); return true; }
                @Override public boolean onQueryTextChange(String t) { debounceSearch(t == null ? "" : t); return true; }
            });
            return;
        }

        EditText edit = findFirstEditText(searchBox);
        if (edit != null) {
            edit.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
                @Override public void onTextChanged(CharSequence s, int st, int b, int c) { debounceSearch(s == null ? "" : s.toString()); }
                @Override public void afterTextChanged(Editable s) {}
            });
        }
    }

    private EditText findFirstEditText(View root) {
        if (root instanceof EditText) return (EditText) root;
        if (root instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) root;
            for (int i = 0; i < vg.getChildCount(); i++) {
                EditText e = findFirstEditText(vg.getChildAt(i));
                if (e != null) return e;
            }
        }
        return null;
    }

    private void debounceSearch(String keyword) {
        if (pending != null) handler.removeCallbacks(pending);
        pending = () -> search(keyword);
        handler.postDelayed(pending, 400);
    }

    private void search(String keyword) {
        repo.search(keyword, role, status).observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override public void onChanged(List<User> users) {
                if (users == null) {
                    Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                } else if (users.isEmpty()) {
                    Toast.makeText(getContext(), "Không tìm thấy tài khoản phù hợp.", Toast.LENGTH_SHORT).show();
                    adapter.setItems(new ArrayList<>());
                } else {
                    adapter.setItems(users);
                }
            }
        });
    }

    private void confirmDisable(User user) {
        if (!user.getEnable()) return;
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Vô hiệu hóa tài khoản")
            .setMessage("Bạn có chắc muốn vô hiệu hóa tài khoản này?")
            .setPositiveButton("Đồng ý", (d, w) -> doDisable(user))
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void doDisable(User user) {
        repo.updateStatus(user.getUserId(), "INACTIVE").observe(getViewLifecycleOwner(), ok -> {
            if (ok == null || !ok) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Tài khoản đã bị vô hiệu hóa!", Toast.LENGTH_SHORT).show();
                search("");
            }
        });
    }

    // Adapter
    private static class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.VH> {
        interface Listener { void onDisable(User u); }
        private final Listener listener;
        private final List<User> items = new ArrayList<>();
        AccountAdapter(Listener l) { this.listener = l; }
        void setItems(List<User> data) { items.clear(); items.addAll(data); notifyDataSetChanged(); }

        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
            return new VH(v);
        }
        @Override public void onBindViewHolder(@NonNull VH h, int p) {
            User u = items.get(p);
            h.txtName.setText(u.getFullName());
            h.txtSub.setText(u.getUserName() + " • " +
                (u.getRoleName() == null ? "" : u.getRoleName()) + " • " +
                (u.getEnable() ? "Hoạt động" : "Vô hiệu hóa"));
            h.btnDisable.setVisibility(u.getEnable() ? View.VISIBLE : View.GONE);
            h.btnDisable.setOnClickListener(v -> listener.onDisable(u));
        }
        @Override public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView txtName, txtSub; MaterialButton btnDisable;
            VH(@NonNull View v) { super(v); txtName = v.findViewById(R.id.txtName); txtSub = v.findViewById(R.id.txtRole); btnDisable = v.findViewById(R.id.btnDisable); }
        }
    }
}

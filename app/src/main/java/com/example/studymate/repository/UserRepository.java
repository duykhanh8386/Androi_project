package com.example.studymate.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.studymate.data.local.StudyMateDatabase;
import com.example.studymate.data.local.dao.UserDao;
import com.example.studymate.data.local.entity.User;
import com.example.studymate.utils.HashUtil;
import com.example.studymate.utils.SessionManager;

import java.util.List;

public class UserRepository {
    private final UserDao userDao;
    private final SessionManager session;

    public UserRepository(Context ctx) {
        StudyMateDatabase db = StudyMateDatabase.getInstance(ctx);
        userDao = db.userDao();
        session = new SessionManager(ctx);
    }

    public static class LoginState {
        public enum Status { SUCCESS, INVALID, WRONG_ROLE, INACTIVE, DISABLED, DB_ERROR }
        public Status status;
        public String role;
        public long userId;
        public LoginState(Status s){ this.status = s; }
        public LoginState(Status s, String role, long userId){ this.status = s; this.role = role; this.userId = userId; }
    }

    /** Đăng nhập với vai trò đã chọn trên UI */
    public LoginState login(String username, String password, String selectedRole) {
        try {
            User u = userDao.findByUsername(username);
            if (u == null) return new LoginState(LoginState.Status.INVALID);

            if (u.disabled) return new LoginState(LoginState.Status.DISABLED);
            if (!"ACTIVE".equals(u.status)) return new LoginState(LoginState.Status.INACTIVE);

            String hash = HashUtil.sha256(password);
            if (!hash.equals(u.passwordHash)) return new LoginState(LoginState.Status.INVALID);

            if (selectedRole != null && !selectedRole.equals(u.role)) {
                return new LoginState(LoginState.Status.WRONG_ROLE, u.role, u.id);
            }

            // Lưu session
            session.setUser(u.id, u.role);
            return new LoginState(LoginState.Status.SUCCESS, u.role, u.id);
        } catch (Exception e) {
            return new LoginState(LoginState.Status.DB_ERROR);
        }
    }

    /** Đăng xuất: chỉ xoá session, không đổi status */
    public boolean logout() {
        try {
            session.clearAll();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public LiveData<User> observe(long id){ return userDao.observeUser(id); }

    // Admin features
    public boolean createAccount(String fullname, String email, String username, String phone, String password, String role, boolean active) {
        try {
            if (userDao.countByUsername(username) > 0) return false; // username tồn tại
            User u = new User();
            u.fullName = fullname; u.email = email; u.username = username; u.phone = phone;
            u.passwordHash = HashUtil.sha256(password);
            u.role = role; u.status = active ? "ACTIVE" : "INACTIVE";
            u.disabled = !active;
            userDao.insert(u);
            return true;
        } catch (Exception e) { return false; }
    }

    public LiveData<List<User>> search(String keyword, String role, String status){
        if (keyword == null) keyword = "";
        if (role == null) role = "";
        if (status == null) status = "";
        return userDao.search(keyword, role, status);
    }

    public boolean disable(long userId){
        try { userDao.disable(userId); return true; } catch (Exception e){ return false; }
    }
}

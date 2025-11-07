package com.example.studymate.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF = "studymate.session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROLE = "role";

    private final SharedPreferences sp;

    public SessionManager(Context ctx) {
        sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    // Chọn role ở màn hình đầu
    public void setRole(String role) {
        sp.edit().putString(KEY_ROLE, role).apply();
    }

    public String getRole() {
        return sp.getString(KEY_ROLE, "");
    }

    // Lưu khi login thành công
    public void setUser(long userId, String role) {
        sp.edit().putLong(KEY_USER_ID, userId)
            .putString(KEY_ROLE, role)
            .apply();
    }

    public long getUserId() {
        return sp.getLong(KEY_USER_ID, -1L);
    }

    // Logout: chỉ xoá session
    public void clearAll() {
        sp.edit().clear().apply();
    }
}


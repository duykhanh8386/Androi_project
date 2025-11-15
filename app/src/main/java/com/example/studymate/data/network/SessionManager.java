package com.example.studymate.data.network;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.studymate.MyApplication;

public class SessionManager {

    private static final String PREF_NAME = "StudyMatePrefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";

    private static final String USER_ID = "user_id";

    private static final String USER_ROLE = "user_role";
    private SharedPreferences prefs;

    public SessionManager() {
        // Lấy Context an toàn từ MyApplication
        Context context = MyApplication.getAppContext();
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Lưu token khi đăng nhập thành công
     */
    public void saveAuthToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    public void saveUserId(Long userId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(USER_ID, userId);
        editor.apply();
    }

    public void saveUserRole(String userRole) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_ROLE, userRole);
        editor.apply();
    }

    /**
     * Lấy token để Interceptor sử dụng
     */
    public String getAuthToken() {
        return prefs.getString(KEY_AUTH_TOKEN, null);
    }

    public String getUserRole() {
        return prefs.getString(USER_ROLE, null);
    }

    public Long getUserId() {
        return prefs.getLong(USER_ID, -1);
    }
    /**
     * Xóa token khi đăng xuất
     */
    public void clearUserData() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.remove(USER_ID);
        editor.remove(USER_ROLE);
        editor.apply();
    }
}
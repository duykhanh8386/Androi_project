package com.example.studymate.data.network;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.studymate.MyApplication;

public class SessionManager {

    private static final String PREF_NAME = "StudyMatePrefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";

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

    /**
     * Lấy token để Interceptor sử dụng
     */
    public String getAuthToken() {
        return prefs.getString(KEY_AUTH_TOKEN, null);
    }

    /**
     * Xóa token khi đăng xuất
     */
    public void clearAuthToken() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.apply();
    }
}
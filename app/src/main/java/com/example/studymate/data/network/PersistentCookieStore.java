// File: PersistentCookieStore.java
package com.example.studymate.data.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class PersistentCookieStore implements CookieJar {
    private static final String PREF_NAME = "StudyMateCookiePrefs";
    private static final String KEY_COOKIE = "cookie_";

    private final SharedPreferences prefs;
    private final Map<String, List<Cookie>> cookieStore = new HashMap<>();

    public PersistentCookieStore(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadCookiesFromPrefs();
    }

    private void loadCookiesFromPrefs() {
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(KEY_COOKIE)) {
                String serializedCookie = (String) entry.getValue();
                Cookie cookie = decodeCookie(serializedCookie);
                if (cookie != null) {
                    String host = extractHostFromKey(key);
                    cookieStore.computeIfAbsent(host, k -> new ArrayList<>()).add(cookie);
                }
            }
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        String host = url.host();
        List<Cookie> existingCookies = cookieStore.get(host);
        if (existingCookies != null) {
            existingCookies.removeAll(cookies);
            existingCookies.addAll(cookies);
        } else {
            cookieStore.put(host, new ArrayList<>(cookies));
        }

        // Lưu vào SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        for (Cookie cookie : cookies) {
            String key = KEY_COOKIE + host + "_" + cookie.name();
            editor.putString(key, encodeCookie(cookie));
        }
        editor.apply();
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        String host = url.host();
        List<Cookie> hostCookies = cookieStore.get(host);
        if (hostCookies != null) {
            cookies.addAll(hostCookies);
        }
        return cookies;
    }

    private String encodeCookie(Cookie cookie) {
        return cookie.toString();
    }

    private Cookie decodeCookie(String cookieString) {
        try {
            return Cookie.parse(HttpUrl.get("http://" + cookieString.split(";")[0].split("=")[0]), cookieString);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractHostFromKey(String key) {
        return key.split("_")[1];
    }

    public void clear() {
        cookieStore.clear();
        prefs.edit().clear().apply();
    }
}
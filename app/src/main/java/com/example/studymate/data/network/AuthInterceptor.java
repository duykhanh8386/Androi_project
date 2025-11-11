package com.example.studymate.data.network;

import androidx.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private SessionManager sessionManager;

    public AuthInterceptor() {
        this.sessionManager = new SessionManager();
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        // Lấy request gốc
        Request originalRequest = chain.request();

        // Lấy token từ SharedPreferences
        String token = sessionManager.getAuthToken();

        // Nếu có token, thêm vào header
        if (token != null) {
            Request.Builder builder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token);

            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }

        // Nếu không có token (như khi đang đăng nhập), cho request đi bình thường
        return chain.proceed(originalRequest);
    }
}
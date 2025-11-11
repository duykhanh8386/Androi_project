// app/src/main/java/com/example/studymate/data/network/RetrofitClient.java
package com.example.studymate.data.network;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {
    private RetrofitClient() {}

    // ĐỔI nếu backend không chạy trên máy dev
    private static String BASE_URL = "http://10.0.2.2:8080/";

    private static Retrofit retrofit;
    private static ApiService apiService;
    private static String authToken; // JWT nếu có

    /** Gọi sau khi login để gắn JWT (tùy chọn) */
    public static void setAuthToken(String token) { authToken = token; }
    // ⭐️ HÀM MỚI: Xây dựng OkHttpClient
    private static OkHttpClient buildClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        // ⭐️ THÊM INTERCEPTOR CỦA BẠN VÀO ĐÂY
        httpClientBuilder.addInterceptor(new AuthInterceptor());

        return httpClientBuilder.build();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(buildClient()) // ⭐️ SỬ DỤNG CLIENT ĐÃ BUILD
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Hàm public static để các Repository có thể gọi và dùng


    /** (tùy chọn) đổi baseUrl nếu chạy trên thiết bị thật */
    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        retrofit = null; apiService = null;
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            OkHttpClient ok = new OkHttpClient.Builder()
                .addInterceptor((Interceptor) chain -> {
                    Request original = chain.request();
                    Request.Builder b = original.newBuilder();
                    if (authToken != null && !authToken.trim().isEmpty()) {
                        b.header("Authorization", "Bearer " + authToken);
                    }
                    return chain.proceed(b.build());
                })
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ok)
                .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}

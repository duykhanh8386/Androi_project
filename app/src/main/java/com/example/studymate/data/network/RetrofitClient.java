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
    private static String BASE_URL = "http://192.168.0.102:8080/";

    private static Retrofit retrofit;
    private static ApiService apiService;
    private static String authToken; // JWT nếu có

    /** Gọi sau khi login để gắn JWT (tùy chọn) */
    public static void setAuthToken(String token) { authToken = token; }

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

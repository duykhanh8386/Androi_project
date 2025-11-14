package com.example.studymate.data.network;

// Import cho Cookie
import com.example.studymate.MyApplication;

import java.net.CookieHandler;
import java.net.CookieManager; // ⭐️ DÙNG CÁI NÀY
import okhttp3.JavaNetCookieJar; // ⭐️ DÙNG CÁI NÀY

// Import cho Timeouts
import java.net.CookiePolicy; // ⭐️ (Thêm)
import java.util.concurrent.TimeUnit;

// Import cho OkHttp và Logging
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// ⭐️ (Import BuildConfig của bạn)
import com.example.studymate.BuildConfig;


public class RetrofitClient {

    private static String BASE_URL = "http://10.0.2.2:8080/";

    private static Retrofit retrofit = null;
    private static ApiService apiService = null;
    private static OkHttpClient okHttpClient = null;

    // ⭐️ BƯỚC 1: Thêm một biến static cho CookieManager
    private static CookieManager cookieManager;

    /**
     * Hàm public duy nhất để các Repository gọi
     */
    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getRetrofitInstance().create(ApiService.class);
        }
        return apiService;
    }

    /**
     * Khởi tạo Retrofit (chỉ chạy 1 lần)
     */
    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient()) // SỬ DỤNG OkHttpClient DUY NHẤT
                    .build();
        }
        return retrofit;
    }

    /**
     * Xây dựng OkHttpClient (chỉ chạy 1 lần)
     */
    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {

            // ⭐️ BƯỚC 2: Khởi tạo CookieManager (nếu chưa có)
            if (cookieManager == null) {
                cookieManager = new CookieManager();
                // (Chính sách này chấp nhận TẤT CẢ cookie)
                cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            }

            // Cấu hình Logging Interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG) {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }

            // Xây dựng Client Builder
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    // ⭐️ BƯỚC 3: Sử dụng JavaNetCookieJar (thay vì PersistentCookieStore)
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor);

            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    // ⭐️ BƯỚC 4: THÊM HÀM MỚI NÀY VÀO ⭐️
    /**
     * Xóa sạch cookie (dùng khi logout)
     */
    public static void clearCookies() {
        if (cookieManager != null) {
            // Xóa tất cả cookie trong bộ nhớ (RAM)
            cookieManager.getCookieStore().removeAll();
        }
        // Reset toàn bộ client để đảm bảo không còn cache cũ
        okHttpClient = null;
        apiService = null;
        retrofit = null;
    }

    /**
     * (Hàm setBaseUrl không đổi)
     */
    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        retrofit = null;
        apiService = null;
        okHttpClient = null; // Xóa client cũ để build lại
    }
}
package com.example.studymate.data.network;

// Import cho Cookie
import com.example.studymate.MyApplication;

import java.net.CookieHandler;
import java.net.CookieManager;
import okhttp3.JavaNetCookieJar;

// Import cho Timeouts
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

// Import cho OkHttp và Logging
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor; // ⭐️ THÊM IMPORT NÀY
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitClient {

    // 1. IP của máy tính (giữ nguyên)
    private static String BASE_URL = "http://10.0.2.2:8080/";

    // 2. Biến Singleton
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;
    private static OkHttpClient okHttpClient = null;

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
     * Nó sẽ gọi buildClient()
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
     * Đây là nơi cấu hình MỌI THỨ (Cookie, Log, Timeout)
     */
    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            // 1. Cấu hình Cookie (giữ nguyên)
            PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.getAppContext());

            // ⭐️ TẠO INTERCEPTOR LOGGING TRỰC TIẾP
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 2. Xây dựng Client Builder
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .cookieJar(cookieStore)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor); // ⭐️ Thêm log vào đây

            // 4. Build client
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    /**
     * (Hàm này giữ nguyên nếu bạn cần đổi IP)
     */
    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        retrofit = null;
        apiService = null;
        okHttpClient = null; // Xóa client cũ để build lại
    }
}
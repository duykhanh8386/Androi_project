package com.example.studymate.data.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // ⚠️ BẮT BUỘC THAY ĐỔI
    // Đây là IP của máy tính đang chạy Spring Boot trong mạng LAN của bạn.
    // KHÔNG dùng "localhost" hay "127.0.0.1".
    private static final String BASE_URL = "http://192.168.1.10:8080/";

    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

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
    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getRetrofitInstance().create(ApiService.class);
        }
        return apiService;
    }

    // Phương thức private để khởi tạo Retrofit (chỉ chạy 1 lần)
    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Cấu hình Logging Interceptor để debug (xem log request/response)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Xây dựng OkHttpClient và thêm Interceptor
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            // Xây dựng Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Dùng Gson
                    .client(httpClient.build()) // Thêm OkHttpClient (để log)
                    .build();
        }
        return retrofit;
    }
}

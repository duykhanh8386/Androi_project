package com.example.studymate.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studymate.data.model.User;
import com.example.studymate.data.model.request.CreateUserRequest;
import com.example.studymate.data.model.request.UpdateStatusRequest;
import com.example.studymate.data.network.ApiService;
import com.example.studymate.data.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final ApiService api = RetrofitClient.getApiService();

    public LiveData<List<User>> search(String keyword, String role, String status) {
        MutableLiveData<List<User>> data = new MutableLiveData<>();
        api.searchUsers(keyword, role, status).enqueue(new Callback<List<User>>() {
            @Override public void onResponse(Call<List<User>> call, Response<List<User>> res) {
                if (res.isSuccessful()) data.postValue(res.body());
                else data.postValue(null);
            }
            @Override public void onFailure(Call<List<User>> call, Throwable t) { data.postValue(null); }
        });
        return data;
    }

    public LiveData<User> create(CreateUserRequest req) {
        MutableLiveData<User> data = new MutableLiveData<>();
        api.createUser(req).enqueue(new Callback<User>() {
            @Override public void onResponse(Call<User> call, Response<User> res) {
                data.postValue(res.isSuccessful() ? res.body() : null);
            }
            @Override public void onFailure(Call<User> call, Throwable t) { data.postValue(null); }
        });
        return data;
    }

    public LiveData<Boolean> updateStatus(int userId, String status) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        api.updateUserStatus(userId, status).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> res) {
                data.postValue(res.isSuccessful());
            }
            @Override public void onFailure(Call<Void> call, Throwable t) { data.postValue(false); }
        });
        return data;
    }
}

package com.example.ninjafleet.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ninjafleet.Network.ApiClient;
import com.example.ninjafleet.Network.ApiService;
import com.example.ninjafleet.models.MachineryModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MachineryRepository {

    private ApiService apiService;

    public MachineryRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<MachineryModel> getMachinery(int page, int limit) {
        MutableLiveData<MachineryModel> data = new MutableLiveData<>();
        apiService.getMachinery(page, limit)
                .enqueue(new Callback<MachineryModel>() {
                    @Override
                    public void onResponse(Call<MachineryModel> call, Response<MachineryModel> response) {
                        data.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<MachineryModel> call, Throwable throwable) {
                        data.setValue(null);
                    }
                });

        return data;
    }
}

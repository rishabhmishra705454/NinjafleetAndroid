package com.example.ninjafleet.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ninjafleet.Network.ApiClient;
import com.example.ninjafleet.Network.ApiService;
import com.example.ninjafleet.models.LoginModel;
import com.example.ninjafleet.models.RegisterSuperModel;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

public class AuthRepositories {

    private final ApiService apiService;

    public AuthRepositories() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<RegisterSuperModel> registerUser(@Part MultipartBody.Part aadharFront,
                                                     @Part("farmerName") RequestBody farmerName,
                                                     @Part("mobileNo") RequestBody mobileNo,
                                                     @Part("aadharNo") RequestBody aadharNo,
                                                     @Part("address") RequestBody address,
                                                     @Part("totalLand") RequestBody totalLand,
                                                     @Part("landType") RequestBody landType,
                                                     @Part MultipartBody.Part aadharBack) {
        MutableLiveData<RegisterSuperModel> data = new MutableLiveData<>();

        apiService.registerUser(aadharFront, farmerName, mobileNo, aadharNo, address, totalLand, landType, aadharBack)
                .enqueue(new Callback<RegisterSuperModel>() {
                    @Override
                    public void onResponse(Call<RegisterSuperModel> call, Response<RegisterSuperModel> response) {
                        if (response.isSuccessful()) {
                            // Success response
                            data.setValue(response.body());
                        } else {
                            // Handle unsuccessful responses (like 400, 404, 500, etc.)
                            RegisterSuperModel errorResponse = new RegisterSuperModel();

                            errorResponse.setSuccess(false);
                            errorResponse.setMessage("Error: " + response.message());
                            data.setValue(errorResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterSuperModel> call, Throwable t) {
                        // Handle request failure
                        RegisterSuperModel errorResponse = new RegisterSuperModel();
                        errorResponse.setSuccess(false);
                        errorResponse.setMessage("Request failed: " + t.getMessage());
                        data.setValue(errorResponse);
                    }
                });

        return data;
    }

    public LiveData<LoginModel> loginUser(RequestBody mobileNo) {
        MutableLiveData<LoginModel> data = new MutableLiveData<>();

        apiService.loginUser(mobileNo).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    LoginModel errorModel = new LoginModel();
                    errorModel.setSuccess(false);
                    errorModel.setMessage("Login failed: " + response.message());
                    data.setValue(errorModel);
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                LoginModel errorModel = new LoginModel();
                errorModel.setSuccess(false);
                errorModel.setMessage("Request failed: " + t.getMessage());
                data.setValue(errorModel);
            }
        });

        return data;
    }


}

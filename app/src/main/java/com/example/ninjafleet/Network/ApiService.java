package com.example.ninjafleet.Network;

import com.example.ninjafleet.models.LoginModel;
import com.example.ninjafleet.models.RegisterSuperModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("api/register")
    Call<RegisterSuperModel> registerUser(
            @Part MultipartBody.Part aadharFront,
            @Part("farmerName") RequestBody farmerName,
            @Part("mobileNo") RequestBody mobileNo,
            @Part("aadharNo") RequestBody aadharNo,
            @Part("address") RequestBody address,
            @Part("totalLand") RequestBody totalLand,
            @Part("landType") RequestBody landType,
            @Part MultipartBody.Part aadharBack
    );

    @POST("api/login")
    Call<LoginModel> loginUser(@Body RequestBody mobileNo);

}

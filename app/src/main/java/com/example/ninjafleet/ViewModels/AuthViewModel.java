package com.example.ninjafleet.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ninjafleet.Repositories.AuthRepositories;
import com.example.ninjafleet.models.LoginModel;
import com.example.ninjafleet.models.RegisterSuperModel;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

public class AuthViewModel extends ViewModel {

    private AuthRepositories authRepositories;

    public AuthViewModel() {
        authRepositories = new AuthRepositories();
    }

    public LiveData<RegisterSuperModel> registerUser(@Part MultipartBody.Part aadharFront,
                                                     @Part("farmerName") RequestBody farmerName,
                                                     @Part("mobileNo") RequestBody mobileNo,
                                                     @Part("aadharNo") RequestBody aadharNo,
                                                     @Part("address") RequestBody address,
                                                     @Part("totalLand") RequestBody totalLand,
                                                     @Part("landType") RequestBody landType,
                                                     @Part MultipartBody.Part aadharBack){
        return authRepositories.registerUser(aadharFront,farmerName,mobileNo,aadharNo,address,totalLand,landType,aadharBack);
    }

    public LiveData<LoginModel> loginUser(String mobileNumber) {
        // Create JSON object for the request body
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobileNo", mobileNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Convert JSON object to string
        String jsonString = jsonObject.toString();

        // Create RequestBody with application/json media type
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);

        return authRepositories.loginUser(requestBody);
    }

}

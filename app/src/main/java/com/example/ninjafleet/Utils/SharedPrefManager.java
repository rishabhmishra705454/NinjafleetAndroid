package com.example.ninjafleet.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "MyAppPrefs";
    private static SharedPrefManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Keys for saving data
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_FARMER_NAME = "farmerName";
    private static final String KEY_MOBILE_NO = "mobileNo";
    private static final String KEY_AADHAR_NO = "aadharNo";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_TOTAL_LAND = "totalLand";
    private static final String KEY_LAND_TYPE = "landType";

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    // Save user data after login
    public void saveUserData(String token, int userId, String farmerName, String mobileNo,
                             String aadharNo, String address, String totalLand, String landType) {
        editor.putString(KEY_TOKEN, token);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_FARMER_NAME, farmerName);
        editor.putString(KEY_MOBILE_NO, mobileNo);
        editor.putString(KEY_AADHAR_NO, aadharNo);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_TOTAL_LAND, totalLand);
        editor.putString(KEY_LAND_TYPE, landType);
        editor.apply();
    }

    // Getters for retrieving saved data
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getFarmerName() {
        return sharedPreferences.getString(KEY_FARMER_NAME, null);
    }

    public String getMobileNo() {
        return sharedPreferences.getString(KEY_MOBILE_NO, null);
    }

    public String getAadharNo() {
        return sharedPreferences.getString(KEY_AADHAR_NO, null);
    }

    public String getAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, null);
    }

    public int getTotalLand() {
        return sharedPreferences.getInt(KEY_TOTAL_LAND, 0);
    }

    public String getLandType() {
        return sharedPreferences.getString(KEY_LAND_TYPE, null);
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return getToken() != null;
    }

    // Logout and clear all saved data
    public void logout() {
        editor.clear();
        editor.apply();
    }
}

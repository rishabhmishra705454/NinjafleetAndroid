package com.example.ninjafleet.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ninjafleet.Network.ApiClient;
import com.example.ninjafleet.Network.ApiService;
import com.example.ninjafleet.models.BookingRequest;
import com.example.ninjafleet.models.BookingResponse;
import com.example.ninjafleet.models.GetBookingResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingRepository {

    private static final String TAG = "BookingRepository"; // Tag for logging
    private ApiService apiService;

    public BookingRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<BookingResponse> addBooking(BookingRequest bookingRequest) {
        MutableLiveData<BookingResponse> data = new MutableLiveData<>();

        Log.d(TAG, "Sending booking request: " + bookingRequest.toString()); // Log the request

        apiService.bookMachinery(bookingRequest).enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(@NonNull Call<BookingResponse> call, @NonNull Response<BookingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                    Log.d(TAG, "Booking successful: " + response.body().toString());
                } else {
                    // Log error response details
                    Log.e(TAG, "Booking failed. Response code: " + response.code());
                    Log.e(TAG, "Response message: " + response.message());

                    // If there's an error body, log it as well
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorResponse);
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body: " + e.getMessage());
                        }
                    } else {
                        Log.e(TAG, "No error body received.");
                    }
                    data.setValue(null); // Set to null on failure
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingResponse> call, @NonNull Throwable throwable) {
                Log.e(TAG, "Booking request failed: " + throwable.getMessage());
                data.setValue(null); // Set to null on failure
            }
        });

        return data;
    }

    public LiveData<GetBookingResponse> getBookings(int userId) {
        MutableLiveData<GetBookingResponse> data = new MutableLiveData<>();

        apiService.getBooking(userId).enqueue(new Callback<GetBookingResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetBookingResponse> call, @NonNull Response<GetBookingResponse> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                    Log.d("BookingRepository", "Response: " + response.body().toString());
                } else {
                    Log.d("BookingRepository", "Response body is null");
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetBookingResponse> call, @NonNull Throwable throwable) {
                Log.e("BookingRepository", "Error fetching bookings: " + throwable.getMessage());
                data.setValue(null);
            }
        });

        return data;
    }
}

package com.example.ninjafleet.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ninjafleet.Repositories.BookingRepository;
import com.example.ninjafleet.models.BookingRequest;
import com.example.ninjafleet.models.BookingResponse;
import com.example.ninjafleet.models.GetBookingResponse;

public class BookingViewModel extends ViewModel {

     BookingRepository bookingRepository;

    public BookingViewModel() {
        bookingRepository = new BookingRepository();
    }

    public LiveData<BookingResponse> addBooking(BookingRequest bookingRequest){

        return bookingRepository.addBooking(bookingRequest);
    }

    public LiveData<GetBookingResponse> getBookings(int userId) {
        return bookingRepository.getBookings(userId);
    }
}

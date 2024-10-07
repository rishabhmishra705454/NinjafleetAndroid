package com.example.ninjafleet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ninjafleet.Adapters.BookingAdapter;
import com.example.ninjafleet.Utils.SharedPrefManager;
import com.example.ninjafleet.ViewModels.BookingViewModel;

public class BookingFragment extends Fragment {

    BookingViewModel bookingViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_bookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPrefManager manager = SharedPrefManager.getInstance(getContext());

        // Assuming you have fetched the booking response and have a list of bookings
        bookingViewModel.getBookings(manager.getUserId()).observe(getViewLifecycleOwner(), bookingResponse -> {
            if (bookingResponse != null && bookingResponse.getSuccess()) {
                BookingAdapter adapter = new BookingAdapter(bookingResponse.getData());
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "Failed to fetch bookings", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
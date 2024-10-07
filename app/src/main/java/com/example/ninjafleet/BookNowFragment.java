package com.example.ninjafleet;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.ninjafleet.Utils.SharedPrefManager;
import com.example.ninjafleet.ViewModels.BookingViewModel;
import com.example.ninjafleet.ViewModels.MachineryViewModel;
import com.example.ninjafleet.databinding.FragmentBookNowBinding;
import com.example.ninjafleet.models.BookingRequest;
import com.example.ninjafleet.models.MachineryModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookNowFragment extends Fragment {


    private FragmentBookNowBinding binding;
    private String startdate , enddate ;
    BookingViewModel bookingViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("markerDataKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // Retrieve the JSON string
                String markerData = bundle.getString("markerData");
                binding.fieldData.setText(markerData);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookNowBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MachineryModel.Machinery machinery = (MachineryModel.Machinery) getArguments().getSerializable("list");
         bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);


        binding.pricing.setText( "\u20B9"+ machinery.getPricing()+" /Hour");
        binding.addLandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });


        binding.pickstartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Format the date
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(selectedYear, selectedMonth, selectedDay);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                               startdate = sdf.format(selectedDate.getTime());
                                // Set the formatted date to the button text
                                binding.pickstartdate.setText(sdf.format(selectedDate.getTime()));
                            }
                        }, year, month, day);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        binding.piclenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Format the date
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(selectedYear, selectedMonth, selectedDay);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                                enddate = sdf.format(selectedDate.getTime());
                                // Set the formatted date to the button text
                                binding.piclenddate.setText(sdf.format(selectedDate.getTime()));
                            }
                        }, year, month, day);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        binding.backBtn.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());
        binding.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String totalLand = binding.totalLand.getEditText().getText().toString();
               String markerData = binding.fieldData.getText().toString().trim();

                if (markerData.isEmpty()) {
                    Toast.makeText(getContext(), "Please select the field by marking its boundary", Toast.LENGTH_SHORT).show();
                } else if (totalLand.isEmpty()) {
                    binding.totalLand.setError("Please enter total land area");
                } else if (startdate==null) {
                    Toast.makeText(getContext(), "Please select a start date", Toast.LENGTH_SHORT).show();
                } else if (enddate==null) {
                    Toast.makeText(getContext(), "Please select an end date", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPrefManager manager = SharedPrefManager.getInstance(getContext());

                    BookingRequest bookingRequest = new BookingRequest(manager.getUserId(),machinery.getId(),machinery.getProviderId(),markerData,totalLand,startdate,enddate ,machinery.getPricing());

                    bookingViewModel.addBooking(bookingRequest).observe(getViewLifecycleOwner(), bookingResponse -> {
                        if (bookingResponse != null) {
                            // Check if the booking was successful and show the message
                            Toast.makeText(getContext(), bookingResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("data", String.valueOf(bookingResponse.getSuccess()));

                            if (bookingResponse.getSuccess()){
                                NavOptions navOptions = new NavOptions.Builder()
                                        .setPopUpTo(R.id.nav, true) // Replace with your start destination ID
                                        .build();

                                // Navigate to the booking page
                                Navigation.findNavController(binding.getRoot())
                                        .navigate(R.id.homeFragment, null, navOptions);
                            }
                        } else {
                            // Handle the case where bookingResponse is null
                            Toast.makeText(getContext(), "Booking response is null. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e("BookingError", "Booking response is null");
                        }
                    });
                }
            }
        });
    }

    private void checkLocation() {
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        if (isLocationEnabled()) {
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.landMarkFragment);

                        } else {
                            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getContext())
                                    .setTitle("GPS Permission")
                                    .setMessage("GPS is required for this app to work .  Please enable GPS")
                                    .setPositiveButton("yes", ((dialog, which) -> {
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);
                                    }))
                                    .setCancelable(true)
                                    .show();
                        }


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        if (permissionDeniedResponse.isPermanentlyDenied()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied .you need to go setting to allow the permission")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                                            getActivity().startActivity(intent);
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    private boolean isLocationEnabled() {

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }
}
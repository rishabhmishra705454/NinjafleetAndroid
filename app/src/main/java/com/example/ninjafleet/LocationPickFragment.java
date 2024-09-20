package com.example.ninjafleet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ninjafleet.databinding.FragmentLocationPickBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocationPickFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private FusedLocationProviderClient mLocationClient;
    private FragmentLocationPickBinding binding;
    private Geocoder geocoder;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            map.setOnCameraIdleListener(() -> {
                LatLng target = map.getCameraPosition().target;
                Double latitude = target.latitude;
                Double longitude = target.longitude;

                try {
                    geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addressList != null && addressList.size() > 0) {
                        String address = addressList.get(0).getAddressLine(0);
                        String locality = addressList.get(0).getLocality();
                        String pincode = addressList.get(0).getPostalCode();

                        binding.addressText.setText(address);
                        binding.localtyText.setText(locality);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLocationPickBinding.inflate(getLayoutInflater());

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyCsxg49JRl5z4IiZQv9j2mH3B2hvoDtaTY"); // Replace with your Places API key
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        mLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        // Check and request permissions
        checkAndRequestPermissions();

        binding.useLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocationToSharedPreferences(); // Save location data when button is clicked
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        binding.currentLocBtn.setOnClickListener(v -> checkAndRequestPermissions());

        AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (autocompleteSupportFragment != null) {
            autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    LatLng latLng = place.getLatLng();
                    if (latLng != null) {
                        gotoLocation(latLng.latitude, latLng.longitude);
                    }
                }

                @Override
                public void onError(@NonNull Status status) {
                    Toast.makeText(getContext(), status.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLoc(); // Permissions already granted, proceed to get location
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLoc() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            if (isLocationEnabled()) {
                mLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        gotoLocation(location.getLatitude(), location.getLongitude());
                    }
                });
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("GPS Permission")
                        .setMessage("GPS is required for this app to work. Please enable GPS.")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        })
                        .setCancelable(false)
                        .show();
            }
        } else {
            // Request permissions if not already granted
            checkAndRequestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000); // 5 seconds
        mLocationRequest.setFastestInterval(2000); // 2 seconds
        mLocationRequest.setNumUpdates(1);

        mLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                Location mLastLocation = locationResult.getLastLocation();
                gotoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }
        }
    };

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        map.clear(); // Clear existing markers
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Selected Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dot)));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        map.moveCamera(cameraUpdate);
        map.animateCamera(cameraUpdate);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    // Method to save location data to SharedPreferences
    private void saveLocationToSharedPreferences() {
        LatLng target = map.getCameraPosition().target;
        Double latitude = target.latitude;
        Double longitude = target.longitude;

        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

            if (addressList != null && addressList.size() > 0) {
                String address = addressList.get(0).getAddressLine(0);
                String locality = addressList.get(0).getLocality();
                String pincode = addressList.get(0).getPostalCode();

                // Save location data in SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LocationData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("address", address);
                editor.putString("locality", locality);
                editor.putString("pincode", pincode);
                editor.apply();

                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)  // This clears the backstack up to the HomeFragment
                        .build();

                // Navigate to the HomeFragment and clear the backstack
                Navigation.findNavController(getView()).navigate(R.id.action_locationPickFragment2_to_homeFragment, null, navOptions);
              }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to save location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLoc(); // Permission granted, proceed to get location
            } else {
                Toast.makeText(getContext(), "Location permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

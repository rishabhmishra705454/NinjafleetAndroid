package com.example.ninjafleet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.example.ninjafleet.databinding.FragmentLandMarkBinding;
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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LandMarkFragment extends Fragment {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private FusedLocationProviderClient mLocationClient;
    private FragmentLandMarkBinding binding;
    private Geocoder geocoder;
    Polygon polygon = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

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


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                    Marker marker = map.addMarker(markerOptions);
                    latLngList.add(latLng);
                    markerList.add(marker);
                }
            });

//            polygon.setFillColor(Color.argb(150, 0, 255, 0)); // Set the fill color (green with alpha)
            binding.drawLand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (markerList.size() < 4) {
                        Toast.makeText(getContext(), "Please add four mark on your field boundary", Toast.LENGTH_SHORT).show();
                    } else {
                        // Remove the existing polygon if there is one
                        if (polygon != null) {
                            polygon.remove();
                        }

                        // Create a new PolygonOptions object with your latLngList
                        PolygonOptions polygonOptions = new PolygonOptions()
                                .addAll(latLngList)
                                .clickable(true);

                        // Add the polygon to the map, and now 'polygon' is not null anymore
                        polygon = map.addPolygon(polygonOptions);

                        binding.drawLand.setVisibility(View.GONE);
                        binding.doneBtn.setVisibility(View.VISIBLE);
                        // Set the fill and stroke colors on the newly created polygon
                        if (polygon != null) {
                            polygon.setFillColor(Color.argb(150, 0, 255, 0)); // Semi-transparent green fill
                            polygon.setStrokeColor(Color.argb(255, 0, 128, 0)); // Solid green stroke

                        }
                    }

                }
            });

            binding.doneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create a JSONArray to hold all the marker data
                    JSONArray markerArray = new JSONArray();

                    try {
                        for (Marker marker : markerList) {
                            JSONObject markerData = new JSONObject();
                            markerData.put("latitude", marker.getPosition().latitude);
                            markerData.put("longitude", marker.getPosition().longitude);
                            markerArray.put(markerData);
                        }

                        // Create a bundle to pass data
                        Bundle result = new Bundle();
                        result.putString("markerData", markerArray.toString());

                        // Use FragmentManager to set the result and pass it to the previous fragment
                        getParentFragmentManager().setFragmentResult("markerDataKey", result);

                        // Optionally, navigate back to the previous fragment
                        getParentFragmentManager().popBackStack();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            binding.clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (polygon != null) {
                        polygon.remove();
                    }

                    for (Marker marker : markerList) {
                        marker.remove();
                    }
                    latLngList.clear();
                    markerList.clear();
                    binding.drawLand.setVisibility(View.VISIBLE);
                    binding.doneBtn.setVisibility(View.GONE);
                }
            });


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLandMarkBinding.inflate(getLayoutInflater());

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

//        binding.useLocationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              //  saveLocationToSharedPreferences(); // Save location data when button is clicked
//            }
//        });
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
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
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
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings mUiSettings = map.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
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
                editor.putString("latitude", String.valueOf(latitude));
                editor.putString("longitude", String.valueOf(longitude));
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
package com.example.ninjafleet;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.ninjafleet.Adapters.MachineryTabAdapter;
import com.example.ninjafleet.Utils.SharedPrefManager;
import com.example.ninjafleet.ViewModels.AuthViewModel;
import com.example.ninjafleet.ViewModels.MachineryViewModel;
import com.example.ninjafleet.databinding.FragmentHomeBinding;
import com.example.ninjafleet.models.MachineryModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    View view;

    private GoogleMap mMap;

    private UiSettings mUiSettings;
    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;

    private static final int LOCATION_LAYER_PERMISSION_REQUEST_CODE = 2;
    private boolean mLocationPermissionDenied = false;

    private double latitude =0 ;
    private double longitude =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        view = binding.getRoot();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (!SharedPrefManager.getInstance(requireContext()).isLoggedIn()) {
                String phone = currentUser.getPhoneNumber();
                Bundle bundle = new Bundle();
                bundle.putString("phoneNo", phone);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_updateProfileFragment2, bundle);
            }
        }


        MachineryViewModel machineryViewModel = new ViewModelProvider(this).get(MachineryViewModel.class);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LocationData", Context.MODE_PRIVATE);

// Retrieve latitude and longitude as strings and validate them
        String latitudeStr = sharedPreferences.getString("latitude", "");
        String longitudeStr = sharedPreferences.getString("longitude", "");

// Validate latitude and longitude
        if (latitudeStr == null || latitudeStr.isEmpty()) {
            latitude = 0.0;
        } else {
            try {
                latitude = Double.valueOf(latitudeStr);
            } catch (NumberFormatException e) {
                latitude = 0.0; // Fallback to default value if parsing fails
            }
        }

        if (longitudeStr == null || longitudeStr.isEmpty()) {
            longitude = 0.0;
        } else {
            try {
                longitude = Double.valueOf(longitudeStr);
            } catch (NumberFormatException e) {
                longitude = 0.0; // Fallback to default value if parsing fails
            }
        }

// Retrieve address
        String address = sharedPreferences.getString("address", null);

// You can now safely use latitude and longitude without worrying about app crashes

        if (address == null || address.isEmpty()) {
            // If location is not saved, navigate to PermissionFragment
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_permissionFragment);
        } else {
            // If location is already saved, proceed normally
            binding.addressText.setText(address);
        }

        binding.changeBtn.setOnClickListener(v -> {
            checkLocation();
        });

        binding.notiBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_notificationFragment));

        machineryViewModel.getMachinery(1, 100).observe(getViewLifecycleOwner(), machineryModel -> {
            Toast.makeText(getContext(), machineryModel.getMessage(), Toast.LENGTH_SHORT).show();

            List<String> categories = new ArrayList<>();
            Map<String, List<MachineryModel.Machinery>> categorizedMachinery = new HashMap<>();

            // Iterate over the machinery items
            for (MachineryModel.Machinery machinery : machineryModel.getData().getMachinery()) {
                String category = machinery.getCategory();

                // Add unique category to the categories list
                if (!categories.contains(category)) {
                    categories.add(category);
                }

                // Categorize the machinery by its category
                if (!categorizedMachinery.containsKey(category)) {
                    categorizedMachinery.put(category, new ArrayList<>());
                }
                categorizedMachinery.get(category).add(machinery);
            }

            // Now you can use the categories list and the categorizedMachinery map as needed
            // For example, print out the categories and their respective machinery
            for (String category : categories) {
                Log.d("MachineryCategory", "Category: " + category + ", Items: " + categorizedMachinery.get(category).get(0).getMachineryName());
            }

            MachineryTabAdapter machineryTabAdapter = new MachineryTabAdapter(getActivity(), categories,categorizedMachinery);
            binding.pager.setAdapter(machineryTabAdapter);
            new TabLayoutMediator(binding.tabLayout, binding.pager,
                    (tab, position) -> {
                tab.setText( categories.get(position));
                tab.getOrCreateBadge().setNumber(categorizedMachinery.get(categories.get(position)).size());
//                tab.setIcon(R.drawable.ic_home);
            }
            ).attach();

        });



    }


    private void checkLocation() {
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        if (isLocationEnabled()) {
                            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_locationPickFragment2);

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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap=googleMap;
        LatLng myLocation = new LatLng(latitude,longitude);

        googleMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("My Selected Location"));
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,14.0f));

        mUiSettings = mMap.getUiSettings();
       mUiSettings.setZoomControlsEnabled(true);

       binding.myLocationBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,14.0f));

           }
       });



    }
}


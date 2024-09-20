package com.example.ninjafleet;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ninjafleet.databinding.FragmentPermissionBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class PermissionFragment extends Fragment {
private FragmentPermissionBinding binding;
private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentPermissionBinding.inflate(getLayoutInflater());
        view = binding.getRoot();

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });
        return view;
    }

    private void checkLocation() {
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        if (isLocationEnabled()) {
                            NavOptions navOptions = new NavOptions.Builder()
                                    .setPopUpTo(R.id.permissionFragment, true)  // Remove PermissionFragment from backstack
                                    .build();

                            Navigation.findNavController(view).navigate(R.id.action_permissionFragment_to_locationPickFragment2, null, navOptions);

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
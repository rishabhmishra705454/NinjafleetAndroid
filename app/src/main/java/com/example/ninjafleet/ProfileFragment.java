package com.example.ninjafleet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ninjafleet.Utils.SharedPrefManager;
import com.example.ninjafleet.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

       private FragmentProfileBinding binding;
       FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding  = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // If user is logged in, navigate to HomeFragment
            Navigation.findNavController(view).navigate(R.id.loginFragment);
        }
        SharedPrefManager manager = SharedPrefManager.getInstance(getContext());
        binding.fullNameText.setText(manager.getFarmerName());
        binding.emailText.setText(manager.getMobileNo());

        binding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.logout();
                FirebaseAuth.getInstance().signOut();
                SharedPreferences locationPreferences = getActivity().getSharedPreferences("LocationData", Context.MODE_PRIVATE);
                SharedPreferences.Editor locationEditor = locationPreferences.edit();
                locationEditor.clear(); // Clear all location data
                locationEditor.apply();


                Navigation.findNavController(view).navigate(R.id.loginFragment);

            }
        });
        return view;
    }
}
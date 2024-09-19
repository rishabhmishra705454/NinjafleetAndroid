package com.example.ninjafleet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ninjafleet.Utils.SharedPrefManager;
import com.example.ninjafleet.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = mAuth.getCurrentUser();
         SharedPrefManager.getInstance(getContext()).isLoggedIn();
        if (currentUser != null ) {
            if ( !SharedPrefManager.getInstance(getContext()).isLoggedIn()){
                String phone  = mAuth.getCurrentUser().getPhoneNumber();
                Bundle bundle = new Bundle();
                bundle.putString("phoneNo",phone);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_updateProfileFragment2,bundle);
            }

        }
    }
}
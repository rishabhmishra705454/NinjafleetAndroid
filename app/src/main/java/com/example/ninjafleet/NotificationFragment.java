package com.example.ninjafleet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ninjafleet.databinding.FragmentNotificationBinding;

public class NotificationFragment extends Fragment {

    FragmentNotificationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding  = FragmentNotificationBinding.inflate(getLayoutInflater());

        binding.backBtn.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack());
        return binding.getRoot();
    }
}
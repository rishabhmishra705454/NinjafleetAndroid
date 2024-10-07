package com.example.ninjafleet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ninjafleet.databinding.FragmentMachineryDetailsBinding;
import com.example.ninjafleet.models.MachineryModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class MachineryDetailsFragment extends Fragment {

    private FragmentMachineryDetailsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMachineryDetailsBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MachineryModel.Machinery machinery = (MachineryModel.Machinery) getArguments().getSerializable("list");


        if (machinery.getImages().get(0)!= null){
            Picasso.get().load("https://ninjafleet-gweyhfetapb5eugk.centralindia-01.azurewebsites.net/"+machinery.getImages().get(0)).into(binding.machineryImage);
        }
        if (machinery.getMachineryName()!=null){
            binding.machineryName.setText(machinery.getMachineryName());
        }
        binding.machineryCategory.setText(machinery.getCategory());
        binding.machineryPricing.setText("\u20B9"+ machinery.getPricing() + " /Hour");
        binding.machineryModel.setText(machinery.getModel());
        binding.machineryCapacity.setText(machinery.getCapacity());
        binding.machineryFueltype.setText(String.valueOf(machinery.getFuelType()));
        binding.machineryUsageRestriction.setText(machinery.getUsageRestrictions());
        binding.machineryWorkingDays.setText(machinery.getAvailableDays());
        binding.machineryCondition.setText(machinery.getCondition());
        binding.machineryInsurance.setText(String.valueOf(machinery.getInsuranceStatus()));
        binding.machineryOperatorRequired.setText(String.valueOf(machinery.getOperatorRequired()));
//        binding.machineryAvailabilityStart.setText(String.valueOf(machinery.getAvailabilityStart()));
//        binding.machineryAvailabilityEnd.setText(String.valueOf(machinery.getAvailabilityEnd()));
        binding.machineryMaintenanceSchedule.setText(machinery.getMaintenanceSchedule());

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) machinery);
                Navigation.findNavController(view).navigate(R.id.bookNowFragment,bundle);
            }
        });

    }
}
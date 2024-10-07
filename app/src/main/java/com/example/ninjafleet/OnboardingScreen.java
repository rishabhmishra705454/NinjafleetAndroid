package com.example.ninjafleet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ninjafleet.Adapters.OnboardingAdapter;
import com.example.ninjafleet.databinding.ActivityOnboardingScreenBinding;
import com.example.ninjafleet.models.OnboardingModel;

import java.util.ArrayList;
import java.util.List;

public class OnboardingScreen extends AppCompatActivity {

    ActivityOnboardingScreenBinding binding;
    private OnboardingAdapter onboardingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setOnboardingItem();
        binding.onboardingViewPager.setAdapter(onboardingAdapter);
        setOnboadingIndicator();
        setCurrentOnboardingIndicators(0);

        binding.onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicators(position);
            }
        });

        binding.buttonOnBoardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (binding.onboardingViewPager.getCurrentItem()+1<onboardingAdapter.getItemCount()){
                    binding.onboardingViewPager.setCurrentItem(binding.onboardingViewPager.getCurrentItem() + 1);
                }else {
                     SharedPreferences sharedPreferences = getSharedPreferences("NINJAVALUES", MODE_PRIVATE);
                     SharedPreferences.Editor myEdit = sharedPreferences.edit();
                     // write all the data entered by the user in SharedPreference and apply
                     myEdit.putBoolean("isOnboardingShown",true);
                     myEdit.apply();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void setOnboadingIndicator() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            binding.layoutOnboardingIndicators.addView(indicators[i]);
        }
}

    @SuppressLint("SetTextI18n")
    private void setCurrentOnboardingIndicators(int index) {
        int childCount = binding.layoutOnboardingIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.layoutOnboardingIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
            }
        }
        if (index == onboardingAdapter.getItemCount() - 1){
            binding.buttonOnBoardingAction.setText("Get Started");
        }else {
            binding.buttonOnBoardingAction.setText("Next");
        }
    }

    private void setOnboardingItem() {
        List<OnboardingModel> onBoardingItems = new ArrayList<>();

        OnboardingModel screen1 = new OnboardingModel();
        screen1.setTitle("Welcome to Ninjafleet");
        screen1.setDescription("Empowering Farmers with Easy Access to Modern Machinery");
        screen1.setImagge(R.drawable.onboarding3);

        OnboardingModel screen2 = new OnboardingModel();
        screen2.setTitle("Rent Machinery, Save Money");
        screen2.setDescription("No need to own expensive equipment. Rent what you need, when you need it, and reduce farming costs with our flexible options.");
        screen2.setImagge(R.drawable.onboarding2);

        OnboardingModel screen3 = new OnboardingModel();
        screen3.setTitle("Real-Time Booking & Tracking");
        screen3.setDescription("Keep track of your booked machinery and ensure timely arrivals with real-time location updates. Manage all your bookings easily from one place.");
        screen3.setImagge(R.drawable.onboarding1);





        onBoardingItems.add(screen1);
        onBoardingItems.add(screen2);
        onBoardingItems.add(screen3);


        onboardingAdapter = new OnboardingAdapter(onBoardingItems);

    }
}
package com.example.ninjafleet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sh = getSharedPreferences("NINJAVALUES", MODE_PRIVATE);
                boolean isOnboardingShown = sh.getBoolean("isOnboardingShown",false);

                if (isOnboardingShown==true){
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    Intent intent = new Intent(SplashScreen.this, OnboardingScreen.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, 3000);


    }
}
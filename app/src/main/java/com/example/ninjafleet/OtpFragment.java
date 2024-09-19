package com.example.ninjafleet;

import android.media.metrics.BundleSession;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ninjafleet.Utils.LoadingDialog;
import com.example.ninjafleet.Utils.SharedPrefManager;
import com.example.ninjafleet.ViewModels.AuthViewModel;
import com.example.ninjafleet.databinding.FragmentOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpFragment extends Fragment {

    private FragmentOtpBinding binding;
    private FirebaseAuth mAuth;
    private String verificationId;

    View view;
    String phone;
    AuthViewModel authViewModel ;
    LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(getLayoutInflater());
         view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        String phoneNumber = getArguments().getString("phoneNumber");
        binding.textPhoneNumber.setText("OTP is send on " + phoneNumber);

         phone = "+91" + phoneNumber;
        sendVerificationCode(phone);
        loadingDialog = new LoadingDialog(getContext());

        binding.verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(binding.otpText.getEditText().getText().toString())){
                    Toast.makeText(getContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                }else if (binding.otpText.getEditText().length() < 6){
                    Toast.makeText(getContext(), "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingDialog.show();
                    verifyCode(binding.otpText.getEditText().getText().toString());
                    binding.countDownText.setVisibility(View.GONE);

                }

            }
        });

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.countDownText.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                binding.progressBar.setVisibility(View.GONE);
                binding.countDownText.setText("Resend OTP");
            }
        }.start();


        return view;
    }

    private void sendVerificationCode(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            final String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                binding.otpText.getEditText().setText(code);
                binding.verifyOtpBtn.setEnabled(false);
                binding.countDownText.setVisibility(View.GONE);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                loadingDialog.show();
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            binding.countDownText.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Successfully Verified", Toast.LENGTH_SHORT).show();


                            authViewModel.loginUser(phone).observe(getViewLifecycleOwner(), loginModel -> {
                                if (loginModel != null) {
                                    if (loginModel.isSuccess()) {

                                        loadingDialog.hide();

                                        Log.e("error","yes");
                                        // User is registered, proceed with the token
                                        SharedPrefManager.getInstance(getContext()).saveUserData(
                                                loginModel.getData().getToken(),
                                                loginModel.getData().getUser().getId(),
                                                loginModel.getData().getUser().getFarmerName(),
                                                loginModel.getData().getUser().getMobileNo(),
                                                loginModel.getData().getUser().getAadharNo(),
                                                loginModel.getData().getUser().getAddress(),
                                                String.valueOf(loginModel.getData().getUser().getTotalLand()),
                                                loginModel.getData().getUser().getLandType()
                                        );
                                       // Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();

                                        NavOptions navOptions = new NavOptions.Builder()
                                                .setPopUpTo(R.id.loginFragment, true)
                                                .build();

                                        Navigation.findNavController(view)
                                                .navigate(R.id.homeFragment, null, navOptions);
                                    } else {
                                        loadingDialog.hide();
                                        // User is not registered or login failed
                                        Log.e("error","no");
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phoneNo",phone);
                                        NavOptions navOptions = new NavOptions.Builder()
                                                .setPopUpTo(R.id.loginFragment, true)
                                                .build();

                                        Navigation.findNavController(view)
                                                .navigate(R.id.updateProfileFragment2, bundle, navOptions);
                                    }
                                } else {
                                    loadingDialog.hide();
                                    Toast.makeText(getContext(), "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });




                        } else {
                            Toast.makeText(getContext(), "Otp not verified", Toast.LENGTH_SHORT).show();
                            loadingDialog.hide();
                        }
                    }
                });
    }
}
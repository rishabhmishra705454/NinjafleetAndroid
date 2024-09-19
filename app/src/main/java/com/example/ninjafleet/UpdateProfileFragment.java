package com.example.ninjafleet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.ninjafleet.Utils.LoadingDialog;
import com.example.ninjafleet.Utils.SharedPrefManager;
import com.example.ninjafleet.ViewModels.AuthViewModel;
import com.example.ninjafleet.databinding.FragmentUpdateProfileBinding;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateProfileFragment extends Fragment {

    private FragmentUpdateProfileBinding binding;
    private View view;
    private File frontImageFile, backImageFile;
    private static final int REQUEST_IMAGE_CAPTURE_FRONT = 1;
    private static final int REQUEST_IMAGE_CAPTURE_BACK = 2;
    private int currentRequestCode;

    private LoadingDialog loadingDialog;


    private ActivityResultLauncher<String[]> requestPermissionsLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // Initialize permission launcher
        requestPermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean cameraGranted = result.get(Manifest.permission.CAMERA);
                    Boolean storageGranted = result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (cameraGranted != null && cameraGranted && storageGranted != null ) {
                        // Permissions granted
                        captureImage(currentRequestCode);
                    } else {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            // Show rationale and request permissions again
                            Toast.makeText(getContext(), "Camera and storage permissions are required. Please grant them.", Toast.LENGTH_SHORT).show();
                            requestPermissionsAgain();
                        } else {
                            Toast.makeText(getContext(), "Permissions denied. You can enable them from settings.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Set phone number from arguments
        String phoneNo = getArguments().getString("phoneNo");
        binding.inputPhone.getEditText().setText(phoneNo);

        loadingDialog = new LoadingDialog(getContext());


        // Set onClick listeners for image capture
        binding.frontImageBtn.setOnClickListener(v -> {
            currentRequestCode = REQUEST_IMAGE_CAPTURE_FRONT;
            checkPermissionsAndCaptureImage();
        });

        binding.backImageBtn.setOnClickListener(v -> {
            currentRequestCode = REQUEST_IMAGE_CAPTURE_BACK;
            checkPermissionsAndCaptureImage();
        });

        binding.contBtn.setOnClickListener(v -> {
            if (validateFields()) {
                loadingDialog.show();

                RequestBody farmerName = RequestBody.create(MediaType.parse("text/plain"), binding.inputName.getEditText().getText().toString().trim());
                RequestBody mobileNo = RequestBody.create(MediaType.parse("text/plain"), binding.inputPhone.getEditText().getText().toString().trim());
                RequestBody aadharNo = RequestBody.create(MediaType.parse("text/plain"), binding.inputAadhar.getEditText().getText().toString().trim());
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), binding.address.getEditText().getText().toString().trim());
                RequestBody totalLand = RequestBody.create(MediaType.parse("text/plain"), binding.totalLand.getEditText().getText().toString().trim());
                RequestBody landType = RequestBody.create(MediaType.parse("text/plain"), ((RadioButton) view.findViewById(binding.radioGroup.getCheckedRadioButtonId())).getText().toString().trim());

                MultipartBody.Part aadharFrontPart = null;
                MultipartBody.Part aadharBackPart = null;

                if (frontImageFile != null) {
                    RequestBody requestFileFront = RequestBody.create(MediaType.parse("image/jpeg"), frontImageFile);
                    aadharFrontPart = MultipartBody.Part.createFormData("aadharFront", frontImageFile.getName(), requestFileFront);
                }

                if (backImageFile != null) {
                    RequestBody requestFileBack = RequestBody.create(MediaType.parse("image/jpeg"), backImageFile);
                    aadharBackPart = MultipartBody.Part.createFormData("aadharBack", backImageFile.getName(), requestFileBack);
                }


                AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

                authViewModel.registerUser(aadharFrontPart, farmerName, mobileNo, aadharNo, address, totalLand, landType, aadharBackPart)
                        .observe(getViewLifecycleOwner(), registerSuperModel -> {
                            loadingDialog.hide();

                            Log.e("##",registerSuperModel.getMessage());
                            if (registerSuperModel != null) {
                                if (registerSuperModel.isSuccess()) {
                                    SharedPrefManager.getInstance(getContext()).saveUserData(
                                            registerSuperModel.getData().getToken(),
                                            registerSuperModel.getData().getUser().getId(),
                                            registerSuperModel.getData().getUser().getFarmerName(),
                                            registerSuperModel.getData().getUser().getMobileNo(),
                                            registerSuperModel.getData().getUser().getAadharNo(),
                                            registerSuperModel.getData().getUser().getAddress(),
                                            registerSuperModel.getData().getUser().getTotalLand(),
                                            registerSuperModel.getData().getUser().getLandType()
                                    );
                                  //  Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();

                                    NavOptions navOptions = new NavOptions.Builder()
                                            .setPopUpTo(R.id.updateProfileFragment2, true)
                                            .build();

                                    Navigation.findNavController(view)
                                            .navigate(R.id.homeFragment, null, navOptions);
                                    // Registration successful
                                    Toast.makeText(getContext(), "Profile Updated successful!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Handle specific error message (e.g., "User is already registered.")
                                    String errorMessage = registerSuperModel.getMessage();

                                        // General error message
                                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                // Handle null response case
                                Toast.makeText(getContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }

    private void checkPermissionsAndCaptureImage() {
        if (!arePermissionsGranted()) {
            requestPermissionsLauncher.launch(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            });
        } else {
            captureImage(currentRequestCode);
        }
    }

    private boolean arePermissionsGranted() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionsAgain() {
        requestPermissionsLauncher.launch(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });
    }

    private void captureImage(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                File imageFile = createImageFile(requestCode);
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.ninjafleet.fileprovider",
                        imageFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Error while creating file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile(int requestCode) throws IOException {
        // Use a unique file name for the front and back images
        String imageFileName;
        if (requestCode == REQUEST_IMAGE_CAPTURE_FRONT) {
            imageFileName = "front_image";
            frontImageFile = File.createTempFile(imageFileName, ".jpg", getActivity().getExternalFilesDir(null));
            return frontImageFile;
        } else {
            imageFileName = "back_image";
            backImageFile = File.createTempFile(imageFileName, ".jpg", getActivity().getExternalFilesDir(null));
            return backImageFile;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE_FRONT && frontImageFile != null) {
                // Display the front image
                Glide.with(this).load(frontImageFile).into(binding.placeholderFront);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE_BACK && backImageFile != null) {
                // Display the back image
                Glide.with(this).load(backImageFile).into(binding.placeholderBack);
            }
        }
    }

    private boolean validateFields() {
        boolean isValid = true;
        if (!validateFarmerName()) isValid = false;
        if (!validatePhoneNumber()) isValid = false;
        if (!validateAadharNumber()) isValid = false;
        if (!validateFrontImage()) isValid = false;
        if (!validateBackImage()) isValid = false;
        if (!validateAddress()) isValid = false;
        if (!validateTotalLand()) isValid = false;
        if (!validateLandType()) isValid = false;
        if (!validateTermsAndConditions()) isValid = false;

        return isValid;
    }
    private boolean validateFarmerName() {
        String val = binding.inputName.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            binding.inputName.setError("Please enter farmer name");
            return false;
        } else {
            binding.inputName.setError(null);
            binding.inputName.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePhoneNumber() {
        String val = binding.inputPhone.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            binding.inputPhone.setError("Please enter phone number");
            return false;
        }
        if (val.length() < 10) {
            binding.inputPhone.setError("Please enter a valid number");
            return false;
        } else {
            binding.inputPhone.setError(null);
            binding.inputPhone.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateAadharNumber() {
        String val = binding.inputAadhar.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            binding.inputAadhar.setError("Please enter Aadhar number");
            return false;
        }
        if (val.length() != 12) {
            binding.inputAadhar.setError("Aadhar number must be 12 digits");
            return false;
        } else {
            binding.inputAadhar.setError(null);
            binding.inputAadhar.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateAddress() {
        String val = binding.address.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            binding.address.setError("Please enter address");
            return false;
        } else {
            binding.address.setError(null);
            binding.address.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateTotalLand() {
        String val = binding.totalLand.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            binding.totalLand.setError("Please enter total land");
            return false;
        } else {
            binding.totalLand.setError(null);
            binding.totalLand.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateLandType() {
        if (binding.radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Please select land type", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateFrontImage() {
        if (frontImageFile == null) {
            Toast.makeText(getContext(), "Please capture front image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateBackImage() {
        if (backImageFile == null) {
            Toast.makeText(getContext(), "Please capture back image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateTermsAndConditions() {
        if (!binding.termsandcondition.isChecked()) {
            Toast.makeText(getContext(), "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

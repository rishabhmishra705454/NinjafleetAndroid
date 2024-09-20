package com.example.ninjafleet.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

/**
 * Utility class for access to runtime permissions.
 */
public abstract class PermissionUtils {

    /**
     * Requests the fine location permission. If the permission has been denied previously, a
     * dialog with additional rationale will be shown.
     */
    public static void requestLocationPermissions(FragmentActivity activity, int requestId,
                                                  boolean finishActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Display a dialog with rationale.
            PermissionUtils.RationaleDialog
                    .newInstance(requestId, finishActivity)
                    .show(activity.getSupportFragmentManager(), "dialog");
        } else {
            // Location permission has not been granted yet, request it directly.
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION}, requestId);
        }
    }

    /**
     * Checks if the result contains a {@link PackageManager#PERMISSION_GRANTED} result for a
     * permission from a runtime permissions request.
     */
    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults,
                                              String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

    /**
     * A dialog that displays a permission denied message.
     */
    public static class PermissionDeniedDialog extends DialogFragment {

        private static final String ARG_FINISH_ACTIVITY = "finish";

        private boolean finishActivity = false;

        public static PermissionDeniedDialog newInstance(boolean finishActivity) {
            Bundle args = new Bundle();
            args.putBoolean(ARG_FINISH_ACTIVITY, finishActivity);

            PermissionDeniedDialog dialog = new PermissionDeniedDialog();
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            finishActivity = getArguments().getBoolean(ARG_FINISH_ACTIVITY);

            return new AlertDialog.Builder(getActivity())
                    .setMessage("Location permission is required for this app to work properly.")
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if (finishActivity) {
                getActivity().finish();
            }
        }
    }

    /**
     * A dialog that explains the use of the location permission and requests the necessary
     * permission.
     */
    public static class RationaleDialog extends DialogFragment {

        private static final String ARGUMENT_PERMISSION_REQUEST_CODE = "requestCode";
        private static final String ARGUMENT_FINISH_ACTIVITY = "finishActivity";

        private boolean finishActivity = false;

        public static RationaleDialog newInstance(int requestCode, boolean finishActivity) {
            Bundle args = new Bundle();
            args.putInt(ARGUMENT_PERMISSION_REQUEST_CODE, requestCode);
            args.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);

            RationaleDialog dialog = new RationaleDialog();
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args = getArguments();
            final int requestCode = args.getInt(ARGUMENT_PERMISSION_REQUEST_CODE);
            finishActivity = args.getBoolean(ARGUMENT_FINISH_ACTIVITY);

            return new AlertDialog.Builder(getActivity())
                    .setMessage("Location permission is needed to show your location on the map.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Request the permission after the user acknowledges the rationale.
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                    requestCode);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if (finishActivity) {
                getActivity().finish();
            }
        }
    }
}

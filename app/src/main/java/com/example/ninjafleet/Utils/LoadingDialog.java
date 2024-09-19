package com.example.ninjafleet.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ProgressBar;

import com.example.ninjafleet.R;

public class LoadingDialog {

    private Dialog dialog;

    public LoadingDialog(Context context) {
        // Initialize the dialog
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false); // Make it non-cancelable
        dialog.setContentView(R.layout.dialog_loading); // Set a custom layout for loading dialog

        // Make the background transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    // Show the loading dialog
    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    // Hide the loading dialog
    public void hide() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}

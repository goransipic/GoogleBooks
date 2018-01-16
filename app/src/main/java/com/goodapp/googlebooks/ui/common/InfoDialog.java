package com.goodapp.googlebooks.ui.common;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.goodapp.googlebooks.BuildConfig;
import com.goodapp.googlebooks.R;

/**
 * Created by gsipic on 16/01/2018.
 */

public class InfoDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.Dialog);
        builder.setMessage( getActivity().getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME);
        builder.setTitle(R.string.about_the_application);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

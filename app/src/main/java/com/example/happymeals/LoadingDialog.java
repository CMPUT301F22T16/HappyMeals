package com.example.happymeals;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    private Activity activity;
    AlertDialog dialog;

    public LoadingDialog(Activity myactivity) {
        activity = myactivity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        try {
            dialog.dismiss();
        } catch (Exception e) {

        }
    }
}
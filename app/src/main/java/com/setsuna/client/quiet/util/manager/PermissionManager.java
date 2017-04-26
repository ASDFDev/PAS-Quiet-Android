package com.setsuna.client.quiet.util.manager;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.setsuna.client.quiet.R;

import static android.support.v4.app.ActivityCompat.requestPermissions;


public class PermissionManager{

    private Context globalContext;


    public PermissionManager(Context context){
        globalContext = context;
    }

    public void showPermission() {
        int REQUEST_PERMISSION_RECORD_AUDIO = 1;
        int permissionCheck = ContextCompat.checkSelfPermission(globalContext, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) globalContext, Manifest.permission.RECORD_AUDIO)) {
                // This will only be shown when user deny mic permission and goes into receiving mode again. This is not shown if "Never shown again" is selected
                showExplanation("Attention!", "Microphone permission is needed to receive code", Manifest.permission.RECORD_AUDIO, REQUEST_PERMISSION_RECORD_AUDIO);
            } else {
                requestPermission(Manifest.permission.RECORD_AUDIO, REQUEST_PERMISSION_RECORD_AUDIO);
            }
        }
    }

    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode) {
        new AlertDialog.Builder(globalContext)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_mic_black_24px)
                .setPositiveButton(globalContext.getResources().getString(android.R.string.ok), (dialog, which) -> requestPermission(permission, permissionRequestCode))
                .create()
                .show();

    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        requestPermissions((Activity)globalContext, new String[]{permissionName}, permissionRequestCode);
    }


}

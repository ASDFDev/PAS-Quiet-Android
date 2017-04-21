package com.setsuna.client.quiet.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.setsuna.client.quiet.signInActivity;

public class ReceiveUtil {

    private Context context;
    private Activity activity;
    private ActivityUtil activityUtil = new ActivityUtil(context);


    public ReceiveUtil(Context context){
        this.context = context;
    }

    public void showAttendanceResults(String title, String message){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog,which) -> {
                    activity.finish();
                    activityUtil.goToSignIn();
                })
                .create()
                .show();
    }

}

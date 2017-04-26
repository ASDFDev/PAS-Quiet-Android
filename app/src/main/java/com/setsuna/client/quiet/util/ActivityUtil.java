package com.setsuna.client.quiet.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.setsuna.client.quiet.lecturer.ReceiveActivity;
import com.setsuna.client.quiet.signInActivity;
import com.setsuna.client.quiet.student.TransmitActivity;

public class ActivityUtil {

    private Context context;

    public ActivityUtil(Context context){
        this.context = context;
    }

    public void goToSignIn(){
        Intent signInIntent = new Intent(context, signInActivity.class);
        context.startActivity(signInIntent);
        ((Activity)context).finish();
    }

    public void goToTransmit(){
        Intent transmitIntent = new Intent(context, TransmitActivity.class);
        context.startActivity(transmitIntent);
        ((Activity)context).finish();
    }

    public void goToReceive(){
        Intent receiveIntent = new Intent(context, ReceiveActivity.class);
        context.startActivity(receiveIntent);
        ((Activity)context).finish();
    }

}

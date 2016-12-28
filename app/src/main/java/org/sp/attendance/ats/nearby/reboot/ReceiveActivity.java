package org.sp.attendance.ats.nearby.reboot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


import org.quietmodem.Quiet.*;

import java.io.IOException;
import java.nio.charset.Charset;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;


/**
 * Created by Daniel on 26/12/2016.
 */


public class ReceiveActivity extends AppCompatActivity {

    private Subscription frameSubscription = Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        subscribeToFrames();
        super.onCreate(savedInstanceState);
    }

    private void subscribeToFrames() {
        frameSubscription.unsubscribe();
        frameSubscription = FrameReceiverObservable.create(this, "ultrasonic-experimental").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(buf -> {
            new AlertDialog.Builder(this)
                    .setTitle("Message Received!")
                    .setMessage(new String(buf,Charset.forName("UTF-8")))
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()
                    .show();
        }, error-> {
        });
    }


}

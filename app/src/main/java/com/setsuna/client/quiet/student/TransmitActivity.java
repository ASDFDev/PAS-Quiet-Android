package com.setsuna.client.quiet.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;


import com.setsuna.client.quiet.R;
import com.setsuna.client.quiet.signInActivity;
import com.setsuna.client.quiet.util.ActivityUtil;
import com.setsuna.client.quiet.util.TransmitUtil;

import org.quietmodem.Quiet.FrameTransmitter;
import org.quietmodem.Quiet.FrameTransmitterConfig;
import org.quietmodem.Quiet.ModemException;

import java.io.IOException;


public class TransmitActivity extends AppCompatActivity {

    private Context context;
    private FrameTransmitter transmitter;
    private FrameTransmitterConfig transmitterConfig;
    private TransmitUtil transmitUtil = new TransmitUtil(context);
    private ActivityUtil activityUtil = new ActivityUtil(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit);
    }

    public TransmitActivity(Context context){
        this.context = context;
    }

    // 9 April 2017: This is really bad code and we should do something about it
    // Let's see how long this stays here....
    // TODO: Decouple UI and logic code.
    public void startBroadcast(View view) {
        String payload = ((EditText) findViewById(R.id.textCode)).getText().toString();
        if (payload.matches("")) {
            Toast.makeText(getApplicationContext(), "You did not enter a code!", Toast.LENGTH_LONG).show();
        } else {
            transmitUtil.changeVolume();
            try {
                transmitterConfig = new FrameTransmitterConfig(context, context.getResources().getString(R.string.quiet_profile));
                transmitter = new FrameTransmitter(transmitterConfig);
                transmitUtil.hideKeyboard();
                (findViewById(R.id.layout_code_input)).setVisibility(ScrollView.GONE);
                (findViewById(R.id.layout_code_broadcasting)).setVisibility(ScrollView.VISIBLE);
                new Thread(() -> {
                    // inifite loop
                    for (int i = 0; i == i; i++) {
                        try {
                            transmitter.send(payload.getBytes());
                        } catch (IOException e) {
                            System.out.println("our message might be too long or the transmit queue full");
                        }
                        transmitUtil.timeDelay(1000);
                    }
                }).start();
            } catch (ModemException me) {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.error)
                        .setIcon(R.drawable.ic_error_outline_black_24px)
                        .setMessage(R.string.modem_exception)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, (dialog,which) ->
                        {
                            finish();
                        })
                        .create()
                        .show();
            } catch (IOException ioe) {
                System.out.println("IOException thrown");
            }
        }
    }

    public void stopBroadcast(View view){
        activityUtil.goToSignIn();
    }

    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void onPause(){
        super.onPause();
        finish();
    }
}

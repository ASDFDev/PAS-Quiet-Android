package com.setsuna.client.quiet;

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


import org.quietmodem.Quiet.FrameTransmitter;
import org.quietmodem.Quiet.FrameTransmitterConfig;
import org.quietmodem.Quiet.ModemException;

import java.io.IOException;

/**
 * Created by Daniel on 26/12/2016.
 */

public class TransmitActivity extends AppCompatActivity {


    private FrameTransmitter transmitter;
    String payload = ((EditText) findViewById(R.id.textCode)).getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit);
    }


    public void startBroadcast(View view) {
        // What abomination is this
        if (payload.matches("")) {
            Toast.makeText(getApplicationContext(), "You did not enter a code!", Toast.LENGTH_LONG).show();
        } else {
            changeVolume();
            FrameTransmitterConfig transmitterConfig;
            try {
                transmitterConfig = new FrameTransmitterConfig(this, "ultrasonic-experimental");
                transmitter = new FrameTransmitter(transmitterConfig);
                hideKeyboard();
                (findViewById(R.id.layout_code_input)).setVisibility(ScrollView.GONE);
                (findViewById(R.id.layout_code_broadcasting)).setVisibility(ScrollView.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // inifite loop
                        for (int i = 0; i == i; i++) {
                            send();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ie) {
                                System.out.println("got interrupted!");
                            }
                        }
                    }
                }).start();
            } catch (ModemException me) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.error)
                        .setMessage("Unfortunately your device does not support modulation")
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
        Intent mainIntent = new Intent(this, signInActivity.class);
        startActivity(mainIntent);
        finish();
    }
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void onPause(){
        super.onPause();
        finish();
    }
    private void hideKeyboard () {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }

    private void send() {
                try {
                    transmitter.send(payload.getBytes());
                } catch (IOException e) {
                    System.out.println("our message might be too long or the transmit queue full");
                }
            }

    private void changeVolume(){
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
    }
}

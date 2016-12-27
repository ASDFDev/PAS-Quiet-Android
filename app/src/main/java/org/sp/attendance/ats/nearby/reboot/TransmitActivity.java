package org.sp.attendance.ats.nearby.reboot;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import org.quietmodem.Quiet.FrameTransmitter;
import org.quietmodem.Quiet.FrameTransmitterConfig;
import org.quietmodem.Quiet.ModemException;

import java.io.IOException;

/**
 * Created by Daniel on 26/12/2016.
 */

public class TransmitActivity extends AppCompatActivity {


    private FrameTransmitter transmitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit);
    }


    public void onSend(View view) {
                changeVolume();
                FrameTransmitterConfig transmitterConfig;
                try{
                   transmitterConfig = new FrameTransmitterConfig(this, "audible");
                    transmitter = new FrameTransmitter(transmitterConfig);
                    hideKeyboard();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 10; i++){
                                send();
                                try{
                                    Thread.sleep(3000);
                                } catch (InterruptedException ie){
                                    System.out.println("got interrupted!");
                                }
                            }
                        }
                    }).start();
                } catch(ModemException me){
                    System.out.println("Modem Exception");
                } catch (IOException ioe){
                    System.out.println("IOException thrown");
                }
    }

    public void onStop(View view){
        Intent mainIntent = new Intent(this, signInActivity.class);
        startActivity(mainIntent);
        finish();
    }
    public void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    public void onPause(){
        finish();
        super.onPause();
    }
        private void hideKeyboard () {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }

        private void send() {
            String payload = ((EditText) findViewById(R.id.numberTextField)).getText().toString();
            if (payload.matches("")) {
                new AlertDialog.Builder(this)
                        .setTitle("Error!")
                        .setMessage("You did not enter a code! Please try again")
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .create()
                        .show();
            } else {
                try {
                    transmitter.send(payload.getBytes());
                    System.out.println(payload);
                } catch (IOException e) {
                    System.out.println("our message might be too long or the transmit queue full");
                }

            }
        }
    private void changeVolume(){
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 70, 0);
    }
}

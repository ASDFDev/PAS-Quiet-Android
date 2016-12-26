package org.sp.attendance.ats.nearby.reboot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
                FrameTransmitterConfig transmitterConfig;
                try{
                   transmitterConfig = new FrameTransmitterConfig(this, "audible");
                    transmitter = new FrameTransmitter(transmitterConfig);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 10; i++){
                                hideKeyboard();
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
        //TODO: kill the audio service
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

        private void hideKeyboard () {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }

        private void send(){
            String payload = ((EditText) findViewById(R.id.numberTextField)).getText().toString();
            try {
                transmitter.send(payload.getBytes());
                System.out.println(payload);

            } catch (IOException e) {
                System.out.println("our message might be too long or the transmit queue full");
            }
        }

}

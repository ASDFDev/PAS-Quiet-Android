package org.sp.attendance.ats.nearby.reboot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


import org.quietmodem.Quiet.*;

import java.io.IOException;


/**
 * Created by Daniel on 26/12/2016.
 */


public class ReceiveActivity extends AppCompatActivity {

  private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        receiveCode(context);
        super.onCreate(savedInstanceState);
    }


    public void receiveCode(final Context context){
        FrameReceiverConfig receiverConfig = null;
        try {
            receiverConfig = new FrameReceiverConfig(this, "ultrasonic-experimental");
        } catch (IOException e) {
            // could not build configs
            System.out.println("Could not build configs");
        }


        FrameReceiver receiver = null;
        try {
            receiver = new FrameReceiver(receiverConfig);
        } catch (ModemException e) {
            // could not set up receiver/transmitter
            System.out.println("Could not setup receiver");

        }
        byte[] buf = new byte[1024];
        long recvLen = 0;
        try {
                recvLen = receiver.receive(buf);
                int i = (int) recvLen;
                System.out.println("___Message is___: " + recvLen);
                new AlertDialog.Builder(context)
                        .setTitle("Message Received!")
                        .setMessage(i)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent mainIntent = new Intent(context, MainActivity.class);
                                context.startActivity(mainIntent);
                            }
                        })
                        .create()
                        .show();
        } catch (IOException e) {
            System.out.println("Read timeout");
            // read timed out
        }
    }

}

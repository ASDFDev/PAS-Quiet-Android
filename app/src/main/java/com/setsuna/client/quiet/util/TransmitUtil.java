package com.setsuna.client.quiet.util;


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.inputmethod.InputMethodManager;

public class TransmitUtil{

    private Context context;

    public TransmitUtil(Context context){
        this.context = context;
    }

    public void timeDelay(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("got interrupted!");
        }
    }

    public void hideKeyboard () {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void changeVolume(){
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
    }

}

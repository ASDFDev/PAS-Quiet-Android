package com.setsuna.client.quiet;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.nio.charset.Charset;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import android.provider.Settings.Secure;
import android.widget.Toast;

import com.setsuna.client.quiet.util.AccountManager;
import com.setsuna.client.quiet.util.ApiManager;
import com.setsuna.client.quiet.util.PermissionManager;


public class ReceiveActivity extends AppCompatActivity {

    private Subscription frameSubscription = Subscriptions.empty();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_receive);
        super.onCreate(savedInstanceState);
        askPermission();
    }

    private void askPermission(){
        PermissionManager permissionManager = new PermissionManager(this);
        permissionManager.showPermission();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        final int REQUEST_PERMISSION_RECORD_AUDIO = 1;
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    subscribeToFrames();
                } else {
                    Toast.makeText(this, R.string.no_permission, Toast.LENGTH_LONG).show();
                    Intent signInIntent = new Intent(this, signInActivity.class);
                    this.startActivity(signInIntent);

                }
        }
    }

    private void subscribeToFrames() {
        frameSubscription = FrameReceiverObservable.create(this, "ultrasonic-experimental").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(buf -> {

            frameSubscription.unsubscribe();
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            String device_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

            ApiManager apiManager = retrofit.create(ApiManager.class);

            ModelSerializer modelSerializer = new ModelSerializer();

            modelSerializer.setDatabase(device_id , AccountManager.loggedInUserID, new String(buf,Charset.forName("UTF-8")));
            Call<ModelSerializer> databaseModelCall = apiManager.insertDatabase(device_id, AccountManager.loggedInUserID, new String(buf,Charset.forName("UTF-8")));
            databaseModelCall.enqueue(new Callback<ModelSerializer>() {
                @Override
                public void onResponse(Call<ModelSerializer> call, Response<ModelSerializer> response) {
                    new AlertDialog.Builder(ReceiveActivity.this)
                            .setTitle("Attendance submission failed!")
                            .setMessage("You have already submitted attendance on this device")
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, (dialog,which) -> {
                                finish();
                                Intent signInIntent = new Intent(ReceiveActivity.this,signInActivity.class);
                                ReceiveActivity.this.startActivity(signInIntent);
                            })
                            .create()
                            .show();
                }
                @Override
                public void onFailure(Call<ModelSerializer> call, Throwable t) {
                    new AlertDialog.Builder(ReceiveActivity.this)
                            .setTitle("Attendance Submitted!")
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, (dialog,which) -> {
                                finish();
                                Intent signInIntent = new Intent(ReceiveActivity.this,signInActivity.class);
                                ReceiveActivity.this.startActivity(signInIntent);
                            })
                            .create()
                            .show();
                }
            });
        }, error-> {
        });
    }

}



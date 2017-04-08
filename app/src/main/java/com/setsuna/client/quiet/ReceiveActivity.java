package com.setsuna.client.quiet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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


/**
 * Created by Daniel on 26/12/2016.
 */


public class ReceiveActivity extends AppCompatActivity {

    private Subscription frameSubscription = Subscriptions.empty();
    private Context context;
    private final int REQUEST_PERMISSION_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
    }


    private void showPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                showExplanation("Permission Needed", "Permission is needed to receive code", Manifest.permission.RECORD_AUDIO, REQUEST_PERMISSION_RECORD_AUDIO);
            } else {
                requestPermission(Manifest.permission.RECORD_AUDIO, REQUEST_PERMISSION_RECORD_AUDIO);
            }
        } else {
           subscribeToFrames();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    subscribeToFrames();
                } else {
                    Toast.makeText(ReceiveActivity.this, R.string.no_permission, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> requestPermission(permission, permissionRequestCode))
                .create()
                .show();

    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, permissionRequestCode);
    }

    private void subscribeToFrames() {
        frameSubscription = FrameReceiverObservable.create(this, "ultrasonic-experimental").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(buf -> {

            frameSubscription.unsubscribe();

            //Network debugging code
            // Enable .client(client.build()) in Retrofit builder
            /*
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(loggingInterceptor);
            */
//TODO REFRACTOR THIS, OMG....
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.base_url))
                    // Enable the next line for debugging network
                    //.client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            String device_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

            ApiManager apiManager = retrofit.create(ApiManager.class);

            ModelSerializer modelSerializer = new ModelSerializer();

            modelSerializer.setDatabase(device_id ,AccountManager.loggedInUserID, new String(buf,Charset.forName("UTF-8")));
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

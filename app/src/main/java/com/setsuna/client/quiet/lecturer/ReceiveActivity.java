package com.setsuna.client.quiet.lecturer;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.setsuna.client.quiet.util.ActivityUtil;
import com.setsuna.client.quiet.util.FrameReceiverObservable;
import com.setsuna.client.quiet.util.ModelSerializer;
import com.setsuna.client.quiet.R;
import com.setsuna.client.quiet.util.manager.AccountManager;
import com.setsuna.client.quiet.util.manager.ApiManager;
import com.setsuna.client.quiet.util.manager.PermissionManager;


public class ReceiveActivity extends AppCompatActivity {

    private Activity activity;
    private Subscription frameSubscription = Subscriptions.empty();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_receive);
        super.onCreate(savedInstanceState);
        PermissionManager permissionManager = new PermissionManager(this);
        permissionManager.showPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[], @NonNull int[] grantResults) {
        final int REQUEST_PERMISSION_RECORD_AUDIO = 1;
        ActivityUtil activityUtil = new ActivityUtil(this);
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    subscribeToFrames();
                } else {
                    Toast.makeText(this, R.string.no_permission, Toast.LENGTH_LONG).show();
                    activityUtil.goToSignIn();
                }
        }
    }

    private void subscribeToFrames() {
        frameSubscription = FrameReceiverObservable.create(ReceiveActivity.this, this.getResources().getString(R.string.quiet_profile)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(buf -> {
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
                    showAttendanceResults("Attendance Submission Failed!", "You have already submitted attendance on this device");
                }
                @Override
                public void onFailure(Call<ModelSerializer> call, Throwable t) {
                    showAttendanceResults("Attendance Submitted!" , "");
                }
            });
        }, error-> {
        });
    }

    private void showAttendanceResults(String title, String message){
        ActivityUtil activityUtil = new ActivityUtil(context);
        new AlertDialog.Builder(ReceiveActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog,which) -> activityUtil.goToSignIn())
                .create()
                .show();
    }

}



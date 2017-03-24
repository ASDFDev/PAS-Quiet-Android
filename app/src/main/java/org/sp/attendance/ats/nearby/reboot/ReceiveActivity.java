package org.sp.attendance.ats.nearby.reboot;

import android.content.Context;
import android.content.Intent;
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


/**
 * Created by Daniel on 26/12/2016.
 */


public class ReceiveActivity extends AppCompatActivity {

    private Subscription frameSubscription = Subscriptions.empty();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        subscribeToFrames();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
    }


    private void subscribeToFrames() {
        frameSubscription.unsubscribe();
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

            DatabaseModel databaseModel = new DatabaseModel();

            databaseModel.setDatabase(device_id ,AccountManager.loggedInUserID, new String(buf,Charset.forName("UTF-8")));
            Call<DatabaseModel> databaseModelCall = apiManager.insertDatabase(device_id, AccountManager.loggedInUserID, new String(buf,Charset.forName("UTF-8")));
            databaseModelCall.enqueue(new Callback<DatabaseModel>() {
                @Override
                public void onResponse(Call<DatabaseModel> call, Response<DatabaseModel> response) {
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
                public void onFailure(Call<DatabaseModel> call, Throwable t) {
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

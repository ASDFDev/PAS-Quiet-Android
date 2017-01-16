package org.sp.attendance.ats.nearby.reboot;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;



/**
 * Created by Daniel on 26/12/2016.
 */


public class ReceiveActivity extends AppCompatActivity {

    private Subscription frameSubscription = Subscriptions.empty();
    private Context context;
    private static String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        subscribeToFrames();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
    }

    private void subscribeToFrames() {
        frameSubscription.unsubscribe();
        frameSubscription = FrameReceiverObservable.create(this, "ultrasonic-experimental").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(buf -> {
        System.out.println("ATS CODE IS: " + new String(buf,Charset.forName("UTF-8")));
//TODO REFRACTOR THIS, OMG....
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://ats.nearby.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiManager apiManager = retrofit.create(ApiManager.class);
            DatabaseModel databaseModel = new DatabaseModel();

            databaseModel.setDatabase(AccountManager.loggedInUserID, deviceID , new String(buf,Charset.forName("UTF-8")));

            Call<DatabaseModel> databaseModelCall = apiManager.insertDatabase(databaseModel.getUsername(), databaseModel.getDevice_id(), databaseModel.getAttendance_code());
            databaseModelCall.enqueue(new Callback<DatabaseModel>() {
                @Override
                public void onResponse(Call<DatabaseModel> call, Response<DatabaseModel> response) {
                    new AlertDialog.Builder(context)
                            .setTitle("Attendance Submitted!")
                            .setMessage(new String(buf,Charset.forName("UTF-8")))
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();
                }

                @Override
                public void onFailure(Call<DatabaseModel> call, Throwable t) {
                    new AlertDialog.Builder(context)
                            .setTitle("Submission failed!")
                            .setMessage("")
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();
                }
            });





        }, error-> {
        });
    }

}

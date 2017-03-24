package org.sp.attendance.ats.nearby.reboot;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by daniel on 24/3/17.
 */

public class StartUp extends AppCompatActivity {

    private final int REQUEST_PERMISSION_RECORD_AUDIO = 1;
    private Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showPermission();
        context = this;
        super.onCreate(savedInstanceState);
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
            check_url();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   check_url();
                } else {
                    Toast.makeText(StartUp.this, R.string.no_permission, Toast.LENGTH_SHORT).show();
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

    private class CheckUrl extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(StartUp.this, "", "Please wait", true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpURLConnection.setFollowRedirects(true);
                HttpURLConnection con = (HttpURLConnection) new URL(params[0]).openConnection();
                con.setRequestMethod("HEAD");
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            boolean bResponse = result;
            if (bResponse) {
                progressDialog.dismiss();
                signIn();
            } else {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.error)
                        .setMessage(R.string.click_here)
                        .setCancelable(false)
                        .setPositiveButton(R.string.here, (dialog, which) ->
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Proximity-Attendance-System/PAS-Backend"));
                            startActivity(browserIntent);
                            System.exit(0);
                        })
                        .create()
                        .show();

            }
        }
    }

    private void signIn(){
        Intent goToSignIn = new Intent(context, signInActivity.class);
        context.startActivity(goToSignIn);
    }

    private void check_url(){
        String BASE_URL = getResources().getString(R.string.base_url);
        CheckUrl url = new CheckUrl();
        url.execute(BASE_URL);
    }

}




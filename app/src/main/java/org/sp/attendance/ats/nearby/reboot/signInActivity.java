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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Daniel on 26/12/2016.
 */

public class signInActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_RECORD_AUDIO=1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_signin);
        super.onCreate(savedInstanceState);
        showPermission();
        context = this;
        String ats_database_url = "http://ats.nearby.com";
        CheckUrl url = new CheckUrl();
        url.execute(ats_database_url);
    }

    public void signIn(View view) {
        if (!((EditText) findViewById(R.id.textEdit_userID)).getText().toString().equals("") && !((EditText) findViewById(R.id.textEdit_password)).getText().toString().equals("")) {
            new AccountManager(signInActivity.this).execute("SignInOnly", ((EditText) findViewById(R.id.textEdit_userID)).getText().toString(),
                    ((EditText) findViewById(R.id.textEdit_password)).getText().toString());
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_sign_in_failed)
                    .setMessage(R.string.error_credentials_disappeared)
                    .setCancelable(false)
                    .setPositiveButton(R.string.dismiss, (dialog,which) -> {
                    })
                    .create()
                    .show();
        }
    }

    private void showPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                showExplanation("Permission Needed","Permission is needed to transmit code", Manifest.permission.RECORD_AUDIO, REQUEST_PERMISSION_RECORD_AUDIO);
            } else {
                requestPermission(Manifest.permission.RECORD_AUDIO, REQUEST_PERMISSION_RECORD_AUDIO);
            }
        } else {
            //yay
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //yay
                } else {
                    Toast.makeText(signInActivity.this, R.string.no_permission, Toast.LENGTH_SHORT).show();
                    System.exit(0);

                }
        }
    }

    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,(dialog,which) -> requestPermission(permission, permissionRequestCode))
                .create()
                .show();

    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    private class CheckUrl extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(signInActivity.this,"","Please wait",true);
        }

        @Override
        protected Boolean doInBackground(String... params){
            try{
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) new URL(params[0]).openConnection();
                con.setRequestMethod("HEAD");
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (Exception e){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result){
            boolean bResponse = result;
            if(bResponse){
                progressDialog.dismiss();
            } else {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.error)
                        .setMessage(R.string.click_here)
                        .setCancelable(false)
                        .setPositiveButton(R.string.here, (dialog,which) ->
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/emansih/ATS_Backend"));
                            startActivity(browserIntent);
                            System.exit(0);
                        })
                        .create()
                        .show();

            }
        }
    }
}

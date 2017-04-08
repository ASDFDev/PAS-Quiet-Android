package com.setsuna.client.quiet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by Daniel on 26/12/2016.
 */

public class signInActivity extends AppCompatActivity {

    private Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_signin);
        context = this;
        CheckUrl url = new CheckUrl();
        url.execute(getResources().getString(R.string.base_url));
        super.onCreate(savedInstanceState);
    }

    public void signIn(View view) {
        if (!((EditText) findViewById(R.id.textEdit_userID)).getText().toString().equals("") &&
                !((EditText) findViewById(R.id.textEdit_password)).getText().toString().equals("")) {
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
    private class CheckUrl extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(signInActivity.this, "", "Please wait", true);
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
            } else {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.error)
                        .setMessage(R.string.click_here)
                        .setCancelable(false)
                        .setPositiveButton(R.string.here, (dialog, which) ->
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ASDFDev/PAS-Backend"));
                            startActivity(browserIntent);
                            finish();
                        })
                        .create()
                        .show();

            }
        }
    }

}

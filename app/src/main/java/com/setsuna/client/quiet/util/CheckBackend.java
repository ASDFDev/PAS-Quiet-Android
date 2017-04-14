package com.setsuna.client.quiet.util;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.setsuna.client.quiet.R;

import java.net.HttpURLConnection;
import java.net.URL;

public class CheckBackend extends AsyncTask<String, Void, Boolean>{

    private ProgressDialog progressDialog;
    private Context globalContext;
    private Activity activity;

        public CheckBackend(Context context){
            globalContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(globalContext);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(globalContext.getResources().getString(R.string.please_wait));
            progressDialog.show();
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
                new AlertDialog.Builder(globalContext)
                        .setTitle(R.string.error)
                        .setMessage(globalContext.getResources().getString(R.string.click_here))
                        .setIcon(R.drawable.ic_error_outline_black_24px)
                        .setCancelable(false)
                        .setPositiveButton(globalContext.getResources().getString(R.string.here), (dialog, which) ->
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ASDFDev/PAS-Backend"));
                            globalContext.startActivity(browserIntent);
                            ((Activity)globalContext).finish();

                        })
                        .create()
                        .show();

            }
        }
    }


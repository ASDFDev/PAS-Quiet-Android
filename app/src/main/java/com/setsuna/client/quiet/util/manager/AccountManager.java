package com.setsuna.client.quiet.util.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.setsuna.client.quiet.R;
import com.setsuna.client.quiet.util.ActivityUtil;


public class AccountManager extends AsyncTask<String, Integer, String> {

    //TODO: Deprecate / Remove this interface. Authenticate via backend instead.
    // Pseudo-accounts manager, provides a small database of pre-set accounts for testing use
    // Can be replaced with actual connection code
	
    public static String loggedInUserID;
    private Activity activity;
    private ProgressDialog progressDialog;
    private Context globalContext;
    private String result;
    private SignInResponse signInState;
    private SignInType signInType;
    private String connectionType;

    public AccountManager(Context context) {
        globalContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        connectionType = params[0];
        if (connectionType.equals("SignInOnly")) {
            try {
                String userID = params[1];
                String password = params[2];
                if (userID.toLowerCase().equals("s10001") && password.equals("staff")) {
                    signInType = SignInType.Staff;
                    signInState = SignInResponse.SignedIn;
                    loggedInUserID = userID.toUpperCase();
                } else if ((userID.toLowerCase().equals("p10000") ||
                        userID.toLowerCase().equals("p10001") ||
                        userID.toLowerCase().equals("p10002") ||
                        userID.toLowerCase().equals("p10003") ||
                        userID.toLowerCase().equals("p10004") ||
                        userID.toLowerCase().equals("p10005") ||
                        userID.toLowerCase().equals("p10006") ||
                        userID.toLowerCase().equals("p10007") ||
                        userID.toLowerCase().equals("p10008") ||
                        userID.toLowerCase().equals("p10009") ||
                        userID.toLowerCase().equals("p10010")) && password.equals("student")) {
                    signInType = SignInType.Student;
                    signInState = SignInResponse.SignedIn;
                    loggedInUserID = userID.toUpperCase();
                } else {
                    signInState = SignInResponse.InvalidCredentials;
                }
            } catch (Exception e) {
                result = (globalContext.getResources().getString((R.string.error_unknown)));
                signInState = SignInResponse.Unknown;
            }
        } else {
            result = globalContext.getResources().getString((R.string.error_connection_invalid));
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(globalContext);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(globalContext.getResources().getString(R.string.please_wait));
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String result1) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                ActivityUtil activityUtil = new ActivityUtil(globalContext);

                if (connectionType.equals("SignInOnly")) {
                    if (signInState.equals(SignInResponse.SignedIn)) {
                        if (signInType == SignInType.Student) {
                           activityUtil.goToTransmit();
                        } else if (signInType == SignInType.Staff) {
                            activityUtil.goToReceive();
                        }
                        updateUI();
                    } else if (signInState.equals(SignInResponse.InvalidCredentials)) {
                        new AlertDialog.Builder(globalContext)
                                .setTitle(R.string.title_sign_in_failed)
                                .setMessage(R.string.error_credentials_invalid)
                                .setCancelable(false)
                                .setPositiveButton(globalContext.getResources().getString(R.string.dismiss), (dialog, which) -> {
                                })
                                .create()
                                .show();
                    }
                }
            }
    }

    private String updateUI() {
        return result;
    }

    private enum SignInResponse {
        SignedIn, InvalidCredentials, Unknown
    }

    private enum SignInType {
        Staff, Student
    }

}

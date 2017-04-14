package com.setsuna.client.quiet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.setsuna.client.quiet.util.AccountManager;
import com.setsuna.client.quiet.util.CheckBackend;

public class signInActivity extends AppCompatActivity{
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_signin);
        super.onCreate(savedInstanceState);
        new CheckBackend(signInActivity.this).execute(getResources().getString(R.string.base_url));
    }

    public void signIn(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        if (!((EditText) findViewById(R.id.textEdit_userID)).getText().toString().equals("") &&
                !((EditText) findViewById(R.id.textEdit_password)).getText().toString().equals("")) {
            new AccountManager(signInActivity.this).execute("SignInOnly", ((EditText) findViewById(R.id.textEdit_userID)).getText().toString(),
                    ((EditText) findViewById(R.id.textEdit_password)).getText().toString());
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_sign_in_failed)
                    .setIcon(R.drawable.ic_error_outline_black_24px)
                    .setMessage(R.string.error_credentials_disappeared)
                    .setCancelable(false)
                    .setPositiveButton(R.string.dismiss, (dialog,which) -> {
                    })
                    .create()
                    .show();
        }
    }

}

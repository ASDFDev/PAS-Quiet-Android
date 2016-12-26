package org.sp.attendance.ats.nearby.reboot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;


/**
 * Created by Daniel on 26/12/2016.
 */

//TODO: call this activity on start instead of MainActivity...
public class signInActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_signin);
        super.onCreate(savedInstanceState);
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
                    .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create()
                    .show();
        }
    }



}

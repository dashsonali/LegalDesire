package com.example.user.legaldesire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.legaldesire.Fragments.Dialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class LoginActivity extends AppCompatActivity {

    private Button usrBtn,lawyerBtn;
    private static int ERROR_DIALOG_REQUEST = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(isServiceOk()){
            init();
        }
        usrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAlreadyLoggedin("user");

            }
        });
        lawyerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAlreadyLoggedin("lawyer");

            }
        });
    }
       public void  init(){
           usrBtn = findViewById(R.id.usrBtn);
           lawyerBtn = findViewById(R.id.lawyerBtn);
        }
    public boolean isServiceOk(){
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LoginActivity.this);
        if(availability== ConnectionResult.SUCCESS)
        {
            Log.d("Result : ","You are good to go!");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(availability)) {
            android.app.Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(LoginActivity.this,availability,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        return false;
    }
    private void checkAlreadyLoggedin(String type) {
        Dialog dialog=new Dialog();
        dialog.type = type;
        dialog.show(getSupportFragmentManager(),"login dialog");
    }
}

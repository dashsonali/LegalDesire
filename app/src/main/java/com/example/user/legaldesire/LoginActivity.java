package com.example.user.legaldesire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.user.legaldesire.fragments.Dialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Button usrBtn,lawyerBtn;
    String typeOfUser;
    private static int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(isServiceOk()){
            init();
        }
        firebaseAuth=FirebaseAuth.getInstance();
       // FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

       if(firebaseAuth.getCurrentUser()!=null&&pref!=null){
            typeOfUser= pref.getString("type",null);

            Log.e("userPresen","firebase not null "+typeOfUser );
            Intent intent=new Intent(this, MainActivity.class);
            intent.putExtra("type",typeOfUser);
            startActivity(intent);
            finish();

           // Toast.makeText(this, typeOfUser, Toast.LENGTH_SHORT).show();
        }
        usrBtn = findViewById(R.id.usrBtn);
        lawyerBtn = findViewById(R.id.lawyerBtn);
        usrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkAlreadyLoggedin("user");
               // Toast.makeText(getApplicationContext(),"User Authentication Pending",Toast.LENGTH_SHORT).show();
               // Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
               // intent.putExtra("user_type","user");
               // startActivity(intent);
            }
        });
        lawyerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAlreadyLoggedin("lawyer");

            }
        });
      //  finish();
    }

    private void checkAlreadyLoggedin(String type) {
        Dialog dialog=new Dialog();
        dialog.type = type;
        dialog.show(getSupportFragmentManager(),"login dialog");

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
    public void  init(){
        usrBtn = findViewById(R.id.usrBtn);
        lawyerBtn = findViewById(R.id.lawyerBtn);
        pref = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
    }
}

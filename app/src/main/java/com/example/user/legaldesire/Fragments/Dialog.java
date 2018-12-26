package com.example.user.legaldesire.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.legaldesire.LoginActivity;
import com.example.user.legaldesire.MainActivity;
import com.example.user.legaldesire.R;
import com.example.user.legaldesire.RegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.zip.Inflater;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by USER on 25-12-2018.
 */

public class Dialog extends AppCompatDialogFragment {
    TextView registertxt,forgot_password;
    EditText email,password;
    Button login ;
    SharedPreferences pref;

    FirebaseAuth mAuth;
   public  static String type;
   ProgressDialog mProgres;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.login_dialog,null);
        builder.setView(view);
       // Toast.makeText(getContext(),type,Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        email=view.findViewById(R.id.entEmail);
        password=view.findViewById(R.id.entPass);
        registertxt=view.findViewById(R.id.registertext);
        login = view.findViewById(R.id.loginBtn);
        forgot_password=view.findViewById(R.id.forgetPassword);
        mProgres = new ProgressDialog(getContext());
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetPassword forgetPassword=new ForgetPassword();
                forgetPassword.show(getFragmentManager(),"forgot password");
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(email.getText()))
                {
                    email.setError("Enter your email!");
                    email.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password.getText()))
                {
                    password.setError("Enter your password!");
                    password.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent;
                        intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("type",type);
                        editor.commit();
                        //getActivity().finish();

                    }
                });

            }
        });
        registertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getActivity().getApplicationContext(),RegistrationActivity.class);
                if(type.equals("lawyer"))
                {

                      intent.putExtra("user_type","lawyer");
                         startActivity(intent);

                }else if(type.equals("user"))
                {

                      intent.putExtra("user_type","lawyer");
                      startActivity(intent);


                }

                //getActivity().finish();
            }
        });
        return builder.create();

    }
}


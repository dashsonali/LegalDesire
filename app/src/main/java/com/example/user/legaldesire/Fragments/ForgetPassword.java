package com.example.user.legaldesire.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.Log;
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

/**
 * Created by USER on 25-12-2018.
 */

public class ForgetPassword extends AppCompatDialogFragment {

    EditText email;
    Button reset ;
    FirebaseAuth mAuth;
    ProgressDialog mProgres;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
       mProgres= new ProgressDialog(getContext());
        View view=inflater.inflate(R.layout.forget_password,null);
        builder.setView(view);
        // Toast.makeText(getContext(),type,Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        email=view.findViewById(R.id.entEmail);
        reset = view.findViewById(R.id.forgetPasswordBtn);
        mProgres = new ProgressDialog(getContext());
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(email.getText()))
                {
                    email.setError("Enter your email!");
                    email.requestFocus();
                    return;
                }
                mProgres.setMessage("Sending Mail");
                mProgres.show();
                String emailstr=email.getText().toString();
                Log.e("email",emailstr);
                mAuth.sendPasswordResetEmail(emailstr)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                mProgres.dismiss();
                            }
                        });


            }
        });

        return builder.create();

    }
}


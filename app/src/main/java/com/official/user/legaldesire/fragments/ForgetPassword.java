package com.official.user.legaldesire.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.official.user.legaldesire.R;

/**
 * Created by USER on 25-12-2018.
 */

public class ForgetPassword extends AppCompatDialogFragment {
    //Hello
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


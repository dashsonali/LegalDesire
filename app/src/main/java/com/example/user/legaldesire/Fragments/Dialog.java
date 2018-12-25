package com.example.user.legaldesire.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.user.legaldesire.R;

import java.util.zip.Inflater;

/**
 * Created by USER on 25-12-2018.
 */

public class Dialog extends AppCompatDialogFragment {
    TextView registertxt;
    EditText email,password;
    Button login ;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.login_dialog,null);
        builder.setView(view);
        email=view.findViewById(R.id.entEmail);
        password=view.findViewById(R.id.entPass);
        registertxt=view.findViewById(R.id.registerBtn);
        registertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return builder.create();

    }
}


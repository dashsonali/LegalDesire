package com.official.user.legaldesire.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.official.user.legaldesire.LoginActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.official.user.legaldesire.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by USER on 25-12-2018.
 */

public class AddressDialog extends AppCompatDialogFragment {
    EditText problem;
    Button send;
    ImageButton close ;
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.address_dialog,null);
        builder.setView(view);
        // Toast.makeText(getContext(),type,Toast.LENGTH_SHORT).show();
        problem=view.findViewById(R.id.entProblem);
        send = view.findViewById(R.id.sendBtn);

        close=view.findViewById(R.id.closebtn);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("clickedclose", "onClick: " );
                dismiss();

                //return;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(problem.getText()))
                {
                    problem.setError("Please Enter Your Workplace Address");
                    problem.requestFocus();
                    return;
                }else{
                    if(getArguments()!=null)
                    {
                        Bundle bundle = getArguments();
                        String longitude = bundle.getString("long");
                        String latitude = bundle.getString("lat");
                        String state = bundle.getString("state");
                        uploadLocation(longitude,latitude,state,problem.getText().toString());
                    }else{
                        Toast.makeText(getContext(),"Tap On A Location First",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        return builder.create();

    }
    public void uploadLocation(String longitude,String latitude,String state,String address)
    {

//
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Lawyers").child(FirebaseAuth
                .getInstance().getCurrentUser().getEmail().replace(".",","));
                   databaseReference.child("location").child("latitude").setValue(latitude);
                  databaseReference.child("location").child("longitude").setValue(longitude);
                  databaseReference.child("location").child("address").setValue(address);
//
               // if (locality != null)
                    databaseReference.child("location").child("state").setValue(state);
               SharedPreferences pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("type", "lawyer");
                editor.putBoolean("dataEntered",false);
                editor.commit();

//                editor.commit();
//                Snackbar snackbar = Snackbar.make(rl, "Your Location Is Uploaded",
//                        Snackbar.LENGTH_INDEFINITE);
//
//                snackbar.show();
               Toast.makeText(getContext(),"Location Uploaded",Toast.LENGTH_SHORT).show();
               startActivity(new Intent(getContext(), LoginActivity.class));
        //  mProgressDialog.dismiss();
    }
}


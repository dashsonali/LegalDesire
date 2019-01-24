package com.example.user.legaldesire.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.models.EmergencyContactDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmergencyContactDialog extends AppCompatDialogFragment {

    Button submit;
    EditText name,contact;
    ImageView closeBtn;
    ProgressDialog mProgressDialog;
    EmergencyContactDataModel emergencyContactDataModel;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.emergeyc_contact_dialog,null);
        name= view.findViewById(R.id.entProblem);
        contact = view.findViewById(R.id.entPhone);
        submit = view.findViewById(R.id.sendBtn);
        mProgressDialog = new ProgressDialog(getContext());
        closeBtn=view.findViewById(R.id.closebtn);

//      closeBtn.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View view) {
//              dismiss();
//          }
//      });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.setMessage("Adding contact..");
                mProgressDialog.show();
                if(TextUtils.isEmpty(contact.getText()))
                {
                    mProgressDialog.dismiss();
                    contact.setError("Enter phone number");
                    contact.requestFocus();
                    return;

                }else if(TextUtils.isEmpty(name.getText()))
                {
                    mProgressDialog.dismiss();
                    name.setError("Enter name");
                    name.requestFocus();
                    return;

                }else{

                        emergencyContactDataModel = new EmergencyContactDataModel(name.getText().toString(),contact.getText().toString());
                       final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance()
                                .getCurrentUser().getEmail().replace(".",","));
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.hasChild("emergency_contact"))
                                {
                                    mProgressDialog.dismiss();
                                    databaseReference.child("emergency_contact").child(databaseReference.push().getKey()).setValue(emergencyContactDataModel);
                                }else{
                                    mProgressDialog.dismiss();
                                    boolean exists = false;
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.child("emergency_contact").getChildren()){

                                        if(dataSnapshot1.child("contact").getValue()
                                                .equals(contact.getText().toString()))
                                        {

                                            Toast.makeText(getActivity(), "Contact Already Exists!", Toast.LENGTH_SHORT).show();
                                           exists=true;
                                        }
                                    }
                                    if(!exists)
                                    {
                                        databaseReference.child("emergency_contact").child(databaseReference.push().getKey()).setValue(emergencyContactDataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(getActivity(),"Contact added!",Toast.LENGTH_SHORT).show();

                                                }else
                                                {
                                                    Toast.makeText(getActivity(),"Something Went Wrong!",Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                }

            }
        });
        builder.setView(view);
        return builder.create();
    }
}

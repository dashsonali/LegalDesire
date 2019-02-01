package com.official.user.legaldesire.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.official.user.legaldesire.R;
import com.official.user.legaldesire.models.EmergencyContactDataModel;
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
    String nameStr,contactStr,keyStr;
    EditText name,contact;
    ImageView closeBtn;
    ProgressDialog mProgressDialog;
    Context mContext;
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

      closeBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              dismiss();
          }
      });
        if(getArguments()!=null)
        {
            nameStr = getArguments().getString("name");
            contactStr = getArguments().getString("contact");
            keyStr = getArguments().getString("key");
            contact.setText(contactStr);
            name.setText(nameStr);


        }
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
                    if(getArguments()!=null)
                    {
                        editContact(keyStr,nameStr,contactStr);
                    }else
                    {
                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance()
                                .getCurrentUser().getEmail().replace(".",","));
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.hasChild("emergency_contact"))
                                {
                                    String key = databaseReference.push().getKey();
                                    mProgressDialog.dismiss();
                                    emergencyContactDataModel = new EmergencyContactDataModel(name.getText().toString(),contact.getText().toString(),key);
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
                                        String key = databaseReference.push().getKey();
                                        emergencyContactDataModel = new EmergencyContactDataModel(name.getText().toString(),contact.getText().toString(),key);

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
                       // emergencyContactDataModel = new EmergencyContactDataModel(name.getText().toString(),contact.getText().toString());

                }

            }
        });
        builder.setView(view);
        return builder.create();
    }
    public void editContact(String keyStr,String nameStr,String contactStr)
    {
        mProgressDialog.dismiss();
        if(name.getText().toString().equals(nameStr)&&contact.getText().toString().equals(contactStr))
        {
            Toast.makeText(mContext,"No edits done!",Toast.LENGTH_SHORT).show();
        }else{
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                                  .getReference()
                                                  .child("Users")
                                                  .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).child("emergency_contact").child(keyStr);
            databaseReference.child("name").setValue(name.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(mContext,"Contact Edited",Toast.LENGTH_SHORT).show();

                        databaseReference.child("contact").setValue(contact.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(mContext,"Contact Edited",Toast.LENGTH_SHORT).show();
                                }else{
                                    Log.e("ERROR",task.getException().toString());
                                    Toast.makeText(mContext,"Something went wrog!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Log.e("ERROR",task.getException().toString());
                        Toast.makeText(mContext,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //databaseReference.child("contact").setValue(contact.getText().toString());

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}

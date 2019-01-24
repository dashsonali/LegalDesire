package com.example.user.legaldesire.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.legaldesire.MainActivity;
import com.example.user.legaldesire.R;
import com.example.user.legaldesire.RegistrationActivity;
import com.example.user.legaldesire.models.LawyerData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by USER on 25-12-2018.
 */

public class BookAppointment extends AppCompatDialogFragment {
    EditText problem;
    Button send;
    ImageButton close ;
    FirebaseAuth mAuth;
    public static LawyerData lawyerData;
    SharedPreferences pref;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.fragment_book_appointment,null);
        builder.setView(view);
        mAuth = FirebaseAuth.getInstance();
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
    //PROBLEM HERE
           send.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(mAuth.getCurrentUser()!=null){
               String mail=lawyerData.getEmail().toString().replace('.',',');
               DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Lawyers").child(mail);
               if(databaseReference.child("pending_appointments").child(mAuth.getCurrentUser().getEmail().replace(".",","))!=null){
                   Toast.makeText(getContext(), "already sent request", Toast.LENGTH_SHORT).show();
               }else{
               databaseReference.child("pending_appointments").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("message").setValue(problem.getText().toString());
               databaseReference.child("pending_appointments").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("status").setValue("-1");
               databaseReference.child("pending_appointments").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("email").setValue(mAuth.getCurrentUser().getEmail());
               databaseReference.child("pending_appointments").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("number").setValue("123456789");
               databaseReference.child("pending_appointments").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("name").setValue("sonali");



                   //New Book
                   DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getEmail().replace(".",","));

               databaseReference1.child("appointments").child(mail).child("message").setValue(problem.getText().toString());
               databaseReference1.child("appointments").child(mail).child("status").setValue("-1");
               databaseReference1.child("appointments").child(mail).child("mail").setValue(mail.replace(",","."));
               databaseReference1.child("appointments").child(mail).child("name").setValue(lawyerData.getName().toString());
               databaseReference1.child("appointments").child(mail).child("number").setValue(lawyerData.getContact().toString());
               databaseReference1.child("appointments").child(mail).child("areaOfPractice").setValue(lawyerData.getAreaOfPractice());






                   dismiss();

           }}
           else {
                   Toast.makeText(getContext(), "no current user", Toast.LENGTH_SHORT).show();
               }
           }
       });
        return builder.create();

    }
}


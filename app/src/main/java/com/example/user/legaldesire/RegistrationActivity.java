package com.example.user.legaldesire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editEmail,editContact,editName,editPass;
    private Spinner areaOfPractice;
    private Button registerBtn;
    private String email,name,contact,pass,user_type,domain;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ProgressDialog mProgressdialog;
    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        editEmail = findViewById(R.id.entEmail);
        editContact = findViewById(R.id.entContact);
        editName = findViewById(R.id.entName);
        editPass = findViewById(R.id.entPass);
          rl = findViewById(R.id.regRelative);

        registerBtn = findViewById(R.id.registerBtn);
        areaOfPractice = findViewById(R.id.areaOfPractice);
        mProgressdialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String areaOfPractice_array[] = getResources().getStringArray(R.array.area_of_practice);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,areaOfPractice_array);

        areaOfPractice.setAdapter(adapter);
       if(getIntent().getExtras().get("user_type").equals("lawyer"))
       {
            user_type = "lawyer";

       }else if(getIntent().getExtras().get("user_type").equals("user"))
       {
           areaOfPractice.setVisibility(View.GONE);
           user_type = "user";
         //  Toast.makeText(getApplicationContext(),"You are a user",Toast.LENGTH_SHORT).show();
       }
       registerBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(TextUtils.isEmpty(editEmail.getText())){
                   editEmail.setError("Please enter your email address!");
                   editEmail.requestFocus();
                   return;
               }
               if(TextUtils.isEmpty(editName.getText())){
                   editName.setError("Please enter your name!");
                   editName.requestFocus();
                   return;
               }
               if(TextUtils.isEmpty(editPass.getText())){
                   editPass.setError("Please enter your password!");
                   editPass.requestFocus();
                   return;
               }
               if(TextUtils.isEmpty(editContact.getText())){
                   editContact.setError("Please enter your password!");
                   editContact.requestFocus();
                   return;
               }
                if(user_type.equals("lawyer")){
                    Log.e("Called","Register Lawyer");
                   registerLawyer();
                }else if(user_type.equals("user")){
                    Log.e("Called","Register Usr");
                   registerUser();
                }

           }
       });

    }
    public void registerLawyer(){
        mProgressdialog.setMessage("Registering lawyer...");
        mProgressdialog.show();
        if(areaOfPractice.getSelectedItem().toString().equals("Area Of Practice")){
            Toast.makeText(getApplicationContext(),"Please Select an area of practice",Toast.LENGTH_SHORT).show();
            return;
        }
        name = editName.getText().toString();
        pass = editPass.getText().toString();
        contact = editContact.getText().toString();
        email = editEmail.getText().toString();
        domain = areaOfPractice.getSelectedItem().toString();
        final DatabaseReference databaseReference = database.getReference().child("Lawyers");
        // DatabaseReference databaseReference = database.getReference().child("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.hasChild(email.replace('.',','))){

                    Snackbar snackbar = Snackbar.make(rl, "Email address is already registered as a Lawyer",
                            Snackbar.LENGTH_INDEFINITE);

                    snackbar.show();

                    mProgressdialog.dismiss();



                    //Toast.makeText(getApplicationContext(),"Email address is already registered as a Lawyer",Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            DatabaseReference databaseReference1 = databaseReference.child(email.replace('.',','));
                            databaseReference1.child("name").setValue(name);
                            databaseReference1.child("email").setValue(email);
                            databaseReference1.child("contact").setValue(contact);
                            databaseReference1.child("areaOfPractice").setValue(domain);
                            databaseReference1.child("uid").setValue(mAuth.getUid());
                            databaseReference1.child("type").setValue("lawyer");
                            mProgressdialog.dismiss();
                            Intent intent = new Intent(RegistrationActivity.this,UploadLocationLawyer.class);
                            intent.putExtra("lawyer_mail",email.replace('.',','));
                            startActivity(intent);
                            finish();

                        }

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void registerUser(){
        mProgressdialog.setMessage("Registering user...");
        mProgressdialog.show();
       // Toast.makeText(getApplicationContext(),"Coming in register user",Toast.LENGTH_SHORT).show();

        name = editName.getText().toString();
        pass = editPass.getText().toString();
        contact = editContact.getText().toString();
        email = editEmail.getText().toString();
        //domain = areaOfPractice.getSelectedItem().toString();
        final DatabaseReference databaseReference = database.getReference().child("Users");
       // DatabaseReference databaseReference = database.getReference().child("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.hasChild(email.replace('.',','))){
                    Snackbar snackbar = Snackbar.make(rl, "Email address is already registered as a User",
                            Snackbar.LENGTH_INDEFINITE);

                    snackbar.show();
                   // Toast.makeText(getApplicationContext(),"Email address is already registered as User",Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            DatabaseReference databaseReference1 = databaseReference.child(email.replace('.',','));
                            databaseReference1.child("name").setValue(name);
                            databaseReference1.child("email").setValue(email);
                            databaseReference1.child("contact").setValue(contact);
                            databaseReference1.child("uid").setValue(mAuth.getUid());
                            databaseReference1.child("type").setValue("user");

                            mProgressdialog.dismiss();
                            finish();

                        }

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

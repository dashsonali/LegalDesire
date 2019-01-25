package com.example.user.legaldesire.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.legaldesire.MainActivity;
import com.example.user.legaldesire.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EditProfileFragment extends Fragment {
  
   
Context mContext;
SharedPreferences sharedPreferences;
    private EditText consultationFeeTxt, editEmail,editContact,editName,editPass;
    private Spinner areaOfPractice,charges;
    private Button registerBtn;
    private String email,name,contact,pass,user_type,domain,chargeStr,consultationFee;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ImageView roundedImage;
    CardView roundedImageView;
    Uri selectImage;

    private static final int RESULT_LOAD_IMAGE = 100;
    ProgressDialog mProgressdialog;
    RelativeLayout rl;
    LinearLayout consultationLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_registration, container, false);
        sharedPreferences = mContext.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        editEmail = view.findViewById(R.id.entEmail);
        editContact = view.findViewById(R.id.entContact);
        editName = view.findViewById(R.id.entName);
        editPass = view.findViewById(R.id.entPass);
        editPass.setVisibility(View.GONE);
        rl = view.findViewById(R.id.regRelative);
        roundedImage = view.findViewById(R.id.roundedImage);
        roundedImageView= view.findViewById(R.id.roundedImageView);
        consultationFeeTxt = view.findViewById(R.id.consultationFeeTxt);
        consultationLayout = view.findViewById(R.id.consultatonFee);
        registerBtn = view.findViewById(R.id.registerBtn);
        areaOfPractice = view.findViewById(R.id.areaOfPractice);
        charges = view.findViewById(R.id.charges);
        mProgressdialog = new ProgressDialog(mContext);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        registerBtn.setText("SUBMIT");
        editEmail.setEnabled(false);
        editEmail.setText(sharedPreferences.getString("email","Enter email"));
        editContact.setText(sharedPreferences.getString("contact","Enter contact"));
        editName.setText(sharedPreferences.getString("name","Enter name"));
        if(sharedPreferences.getString("type",null).equals("user"))
        {
            consultationLayout.setVisibility(View.GONE);
            areaOfPractice.setVisibility(View.GONE);
            roundedImageView.setVisibility(View.GONE);


        }else{
            String consultationFeeStr = sharedPreferences.getString("consultationFee","").substring(0,
                    sharedPreferences.getString("consultationFee","").indexOf("R")-1).trim();
            Toast.makeText(mContext,consultationFeeStr,Toast.LENGTH_SHORT).show();

        }
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference;
                if(sharedPreferences.getString("type",null).equals("user"))
                {
                    if(TextUtils.isEmpty(editName.getText())||TextUtils.isEmpty(editContact.getText()))
                    {
                        Toast.makeText(mContext,"Please fill up all the fields", Toast.LENGTH_LONG);
                        return;
                    }
                    Toast.makeText(mContext,"Profile Edited",Toast.LENGTH_SHORT).show();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(sharedPreferences.getString("email","").replace(".",","));
                    databaseReference.child("name").setValue(editName.getText().toString());
                    databaseReference.child("email").setValue(editEmail.getText().toString());
                    databaseReference.child("contact").setValue(editContact.getText().toString());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name",editName.getText().toString());
                    editor.putString("email",editEmail.getText().toString());
                    editor.putString("contact",editContact.getText().toString());
                   // editor.putBoolean("dataEntered",false);
                    editor.commit();
                   // Toast.makeText(mContext,"Profile Edited!", Toast.LENGTH_LONG);
                    startActivity(new Intent(mContext, MainActivity.class));


                }else{

                }
            }
        });
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
       
    }
}


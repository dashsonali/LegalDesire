package com.official.user.legaldesire;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {

    private EditText consultationFeeTxt, editEmail,editContact,editName,editPass;
    private Spinner areaOfPractice,charges;
    private Button registerBtn;
    private String email,name,contact,pass,user_type,domain,chargeStr,consultationFee;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ImageView roundedImage;
    CardView roundedImageView;
    Uri selectImage;
    TextView tapText;
    private static final int RESULT_LOAD_IMAGE = 100;
    ProgressDialog mProgressdialog;
    RelativeLayout rl;
    LinearLayout consultationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        editEmail = findViewById(R.id.entEmail);
        editContact = findViewById(R.id.entContact);
        editName = findViewById(R.id.entName);
        editPass = findViewById(R.id.entPass);
          rl = findViewById(R.id.regRelative);
          roundedImage = findViewById(R.id.roundedImage);
          roundedImageView= findViewById(R.id.roundedImageView);
          consultationFeeTxt = findViewById(R.id.consultationFeeTxt);
        consultationLayout = findViewById(R.id.consultatonFee);
        registerBtn = findViewById(R.id.registerBtn);
        areaOfPractice = findViewById(R.id.areaOfPractice);
        charges = findViewById(R.id.charges);
        mProgressdialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();
        tapText = findViewById(R.id.tapTextView);
        mAuth = FirebaseAuth.getInstance();
        String areaOfPractice_array[] = getResources().getStringArray(R.array.area_of_practice);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,areaOfPractice_array);

        areaOfPractice.setAdapter(adapter);
        String charges_array[] = getResources().getStringArray(R.array.charges);
        ArrayAdapter<String> charges_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,charges_array);
        charges.setAdapter(charges_adapter);
       if(getIntent().getExtras().get("user_type").equals("lawyer"))
       {
            user_type = "lawyer";

       }else if(getIntent().getExtras().get("user_type").equals("user"))
       {
           consultationLayout.setVisibility(View.GONE);
           areaOfPractice.setVisibility(View.GONE);
           tapText.setVisibility(View.GONE);
           user_type = "user";
         //  Toast.makeText(getApplicationContext(),"You are a user",Toast.LENGTH_SHORT).show();
       }
       roundedImageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(isReadStoragePermissionGranted())
               {
                   uploadImage();
               }

           }
       });
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
        }else if(TextUtils.isEmpty(consultationFeeTxt.getText())){
            Toast.makeText(getApplicationContext(),"Please Enter Your Consultation Fee",Toast.LENGTH_SHORT).show();
            return;
        }
        name = editName.getText().toString();
        pass = editPass.getText().toString();
        contact = editContact.getText().toString();
        email = editEmail.getText().toString();
        domain = areaOfPractice.getSelectedItem().toString();
        chargeStr= charges.getSelectedItem().toString();
        consultationFee = consultationFeeTxt.getText().toString() + " RS/- "+chargeStr;

        final DatabaseReference databaseReference = database.getReference().child("Lawyers");
        // DatabaseReference databaseReference = database.getReference().child("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
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
                            if(task.isSuccessful()){
                                DatabaseReference databaseReference1 = databaseReference.child(email.replace('.',','));
                                databaseReference1.child("name").setValue(name);
                                databaseReference1.child("email").setValue(email);
                                databaseReference1.child("contact").setValue(contact);
                                databaseReference1.child("areaOfPractice").setValue(domain);
                                databaseReference1.child("uid").setValue(mAuth.getUid());
                                databaseReference1.child("type").setValue("lawyer");
                                databaseReference1.child("rating").setValue(0);
                                databaseReference1.child("usersRated").setValue(0);
                                databaseReference1.child("consultation_fee").setValue(consultationFee);
                                Bitmap bitmapImage=null;
                                if(selectImage!=null)
                                {
                                    try {
                                        bitmapImage =MediaStore.Images.Media.getBitmap(getContentResolver() ,selectImage);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    bitmapImage = BitmapFactory.decodeResource(getResources(),R.drawable.lawyer);
                                }
                                compressAndUpload(bitmapImage);

                            }else{
                                try{
                                    mProgressdialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Authentication Failed!",Toast.LENGTH_SHORT).show();
                                    throw task.getException();
                                }catch (Exception e)
                                {
                                    Log.e("Error",e.getMessage());
                                }

                            }


                        }

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void compressAndUpload(Bitmap bitmap)
    {
        mProgressdialog.setMessage("Uploading Profile Pic ...");
        bitmap = Bitmap.createScaledBitmap(bitmap, 500 ,500, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, baos);
        byte[] data = baos.toByteArray();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProfileImages/Lawyers").child(mAuth.getCurrentUser().getEmail());
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                mProgressdialog.dismiss();
                Toast.makeText(getApplicationContext(),"Something Went Wrong!",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                mProgressdialog.dismiss();
                Intent intent = new Intent(RegistrationActivity.this,UploadLocationLawyer.class);
                intent.putExtra("lawyer_mail",mAuth.getCurrentUser().getEmail());
                startActivity(intent);
                // ...
            }
        });

    }
    public void registerUser(){
        mProgressdialog.setMessage("Registering user...");
        mProgressdialog.show();
       //Toast.makeText(getApplicationContext(),"Coming in register user",Toast.LENGTH_SHORT).show();

        name = editName.getText().toString();
        pass = editPass.getText().toString();
        contact = editContact.getText().toString();
        email = editEmail.getText().toString();
        //domain = areaOfPractice.getSelectedItem().toString();
        final DatabaseReference databaseReference = database.getReference().child("Users");
       //DatabaseReference databaseReference = database.getReference().child("Users");
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

                            if(task.isSuccessful()){

                                DatabaseReference databaseReference1 = databaseReference.child(email.replace('.',','));
                                databaseReference1.child("name").setValue(name);
                                databaseReference1.child("email").setValue(email);
                                databaseReference1.child("contact").setValue(contact);
                                databaseReference1.child("uid").setValue(mAuth.getUid());
                                databaseReference1.child("type").setValue("user");
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("type","user");
                                editor.commit();
                                mProgressdialog.dismiss();
                                finish();
                   //             StorageReference ref = FirebaseStorage.getInstance().getReference().child("ProfileImages/Users/"+mAuth.getCurrentUser().getUid());
//                                ref.putFile(selectImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                                        mProgressdialog.dismiss();
//                                        finish();
//                                    }
//                                });


                            }else{
                                try{
                                    mProgressdialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Authentication Failed!",Toast.LENGTH_SHORT).show();
                                }catch (Exception e)
                                {
                                    Log.e("Error",e.getMessage());
                                } }

                        }

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public boolean isReadStoragePermissionGranted(){
        if(Build.VERSION.SDK_INT>=23)
        {
            if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                return true;
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                        ,3);
                return  false;
            }

        }else{
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==3&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            uploadImage();
        }
    }
    public void uploadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==RESULT_LOAD_IMAGE&&resultCode==RESULT_OK&&data!=null)
        {
            Toast.makeText(getApplicationContext(),"Got Data",Toast.LENGTH_SHORT).show();
            selectImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getApplicationContext().getContentResolver().query(selectImage,filePathColumn,
                    null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            roundedImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));



        }
    }


}

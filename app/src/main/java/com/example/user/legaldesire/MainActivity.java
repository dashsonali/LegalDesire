package com.example.user.legaldesire;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.legaldesire.fragments.HomeFragment;
import com.example.user.legaldesire.fragments.LawyerAppointmentFragment;
import com.example.user.legaldesire.fragments.LawyerProfileFragment;
import com.example.user.legaldesire.fragments.LawyerRecycler;
import com.example.user.legaldesire.fragments.LearnLaw;
import com.example.user.legaldesire.fragments.UserAppointmentFragment;
import com.example.user.legaldesire.fragments.UserProfileFrag;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    final HashMap<String,Fragment> fragmentHashMap = new HashMap<>();
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    String typeOfUser;
    ProgressDialog progressDialog;
    boolean dataStored;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView=findViewById(R.id.btm_nav);
        Intent intent = getIntent();
        mAuth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        sharedPreferences= getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        typeOfUser= sharedPreferences.getString("type",null);
        dataStored = sharedPreferences.getBoolean("dataStored",false);
        if(!dataStored)
        {
            Toast.makeText(getApplicationContext(),"Data is Not Stored",Toast.LENGTH_SHORT).show();
          final SharedPreferences.Editor editor = sharedPreferences.edit();
          if(typeOfUser.equals("user"))
          {

              DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                      .child(mAuth.getCurrentUser().getEmail().replace(".",","));
              databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      progressDialog.dismiss();
                      String name = dataSnapshot.child("name").getValue().toString();
                      String email = dataSnapshot.child("email").getValue().toString();
                      String contact = dataSnapshot.child("contact").getValue().toString();
                      editor.putString("name",name);
                      editor.putString("contact",contact);
                      editor.putString("email",email);
                      editor.putBoolean("dataEntered",true);
                      editor.commit();

                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });
          }else {
              DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Lawyers")
                      .child(mAuth.getCurrentUser().getEmail().replace(".",","));
              databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      progressDialog.dismiss();
                      String name = dataSnapshot.child("name").getValue().toString();
                      String email = dataSnapshot.child("email").getValue().toString();
                      String contact = dataSnapshot.child("contact").getValue().toString();
                      String rating = dataSnapshot.child("rating").getValue().toString();
                      String usersRated = dataSnapshot.child("usersRated").getValue().toString();
                      String areaOfPractice = dataSnapshot.child("areaOfPractice").getValue().toString();
                      editor.putString("name",name);
                      editor.putString("contact",contact);
                      editor.putString("email",email);
                      editor.putString("rating",rating);
                      editor.putString("usersRated",usersRated);

                      editor.putBoolean("dataEntered",true);

                      editor.commit();
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });
          }

        }else{
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Data is Stored",Toast.LENGTH_SHORT).show();
        }
        Log.e("currentuser",""+mAuth.getCurrentUser()+" njk" );
        if(intent.getExtras()!=null)
        {
            if(intent.getStringExtra("action")!=null)
            {
                if(intent.getStringExtra("action").equals("search_lawyer"))
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LawyerRecycler()).commit();
                }
            }else{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
            }

        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        }



        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragment=null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectFragment = fragmentHashMap.get("home");
                        if(selectFragment==null)
                        {
                            selectFragment = new HomeFragment();
                            fragmentHashMap.put("home",selectFragment);
                        }

                        break;
                    case R.id.nav_cases:
                        selectFragment = fragmentHashMap.get("user_profile");
                        if(selectFragment==null)
                        {if(typeOfUser.equals("user")){
                            selectFragment = new UserProfileFrag();
                            fragmentHashMap.put("user_profile",selectFragment);}
                              else {
                            selectFragment = new LawyerProfileFragment();
                            fragmentHashMap.put("user_profile",selectFragment);
                             }
                        }

                        break;
                    case R.id.nav_learnLaw:
                        selectFragment = fragmentHashMap.get("learn_law");
                        if(selectFragment==null)
                        {
                            selectFragment = new LearnLaw();
                            fragmentHashMap.put("learn_law",selectFragment);
                        }
                        break;
                    case R.id.nav_appointments:
                        selectFragment = fragmentHashMap.get("appointments");
                        if(selectFragment==null){
                            if(typeOfUser.equals("user")){
                            selectFragment = new UserAppointmentFragment();
                            fragmentHashMap.put("appointments",selectFragment);}
                            else {
                               selectFragment = new LawyerAppointmentFragment();
                                fragmentHashMap.put("appointments",selectFragment);
                            }
                        }

                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();
            return true;
            }
        });
    }


}

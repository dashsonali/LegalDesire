package com.example.user.legaldesire;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.user.legaldesire.fragments.HomeFragment;
import com.example.user.legaldesire.fragments.LawyerAppointmentFragment;
import com.example.user.legaldesire.fragments.LawyerProfileFragment;
import com.example.user.legaldesire.fragments.LawyerRecycler;
import com.example.user.legaldesire.fragments.LearnLaw;
import com.example.user.legaldesire.fragments.UserAppointmentFragment;
import com.example.user.legaldesire.fragments.UserProfileFrag;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    final HashMap<String,Fragment> fragmentHashMap = new HashMap<>();
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    String typeOfUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView=findViewById(R.id.btm_nav);
        Intent intent = getIntent();
        mAuth=FirebaseAuth.getInstance();
        sharedPreferences= getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        typeOfUser= sharedPreferences.getString("type",null);

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

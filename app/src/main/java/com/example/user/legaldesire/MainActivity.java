package com.example.user.legaldesire;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.user.legaldesire.fragments.HomeFragment;
import com.example.user.legaldesire.fragments.OtherFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView=findViewById(R.id.btm_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragment=null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectFragment = new HomeFragment();
                        break;
                    case R.id.nav_cases:
                        selectFragment = new OtherFragment();
                        break;
                    case R.id.nav_learnLaw:
                        selectFragment = new OtherFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();
            return true;
            }
        });
    }


}

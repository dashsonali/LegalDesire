package com.example.user.legaldesire.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.legaldesire.LoginActivity;
import com.example.user.legaldesire.MainActivity;
import com.example.user.legaldesire.R;
import com.example.user.legaldesire.adapters.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by USER on 25-12-2018.
 */

public class UserProfileFrag extends Fragment {

  //  private Button b,findLawyer;

    //private LocationManager locationManager;
   // private LocationListener listener;
    //ProgressDialog progressDialog;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ImageView user_menu;
    TextView welcome,mail,number;
    SharedPreferences sharedPreferences;

    private  int[] tabIcons = {R.drawable.user_menu,
            R.drawable.article_bookmarks,
            R.drawable.favorite_lawyers};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater layoutInflater=getActivity().getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.fragment_other,null);
        welcome=view.findViewById(R.id.welcmtxt);
        number=view.findViewById(R.id.phoneNumber);
        mail=view.findViewById(R.id.email);
        sharedPreferences = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        welcome.setText(sharedPreferences.getString("name",null));
        mail.setText(sharedPreferences.getString("email",null));
        number.setText(sharedPreferences.getString("contact",null));
//

        user_menu = view.findViewById(R.id.user_menu);
        user_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_pop_up(view);
            }
        });

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//



        return view;


    }

    public void show_pop_up(View v){
        PopupMenu popupMenu = new PopupMenu(getActivity().getBaseContext(),v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.logout)
                {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().commit();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    startActivity(new Intent(getActivity().getBaseContext(),LoginActivity.class));
                    getActivity().finish();
                    return true;
                }else if(id == R.id.edit_profile){
                    return true;
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.user_menu);
        popupMenu.show();

    }







    }




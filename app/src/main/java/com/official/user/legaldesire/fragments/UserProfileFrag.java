package com.official.user.legaldesire.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.official.user.legaldesire.LoginActivity;

import com.official.user.legaldesire.R;
import com.official.user.legaldesire.adapters.ViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

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
    private TextView email,phoneNumber,user_name;
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

        user_menu = view.findViewById(R.id.user_menu);
        user_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_pop_up(view);
            }
        });

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        user_name = view.findViewById(R.id.user_name);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("dataEntered",false))
        {

            user_name.setText("WELCOME "+sharedPreferences.getString("name","").toUpperCase());
            email.setText(sharedPreferences.getString("email",""));
            phoneNumber.setText(sharedPreferences.getString("contact",""));
        }
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);




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

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                  //  EditProfileFragment editProfileFragment = new EditProfileFragment();


                    ft.replace(R.id.fragment_container,new EditProfileFragment()).commit();
                    return true;
                }else if(id==R.id.fileRti)
                {
                    FileRtiFragment fileRtiFragment = new FileRtiFragment();
                    fileRtiFragment.show(getFragmentManager(),"");
                }else if(id==R.id.gstApplication)
                {
                    GSTapplicationFragment gsTapplicationFragment = new GSTapplicationFragment();
                    gsTapplicationFragment.show(getFragmentManager(),"");
                }else if(id==R.id.legalDrafting)
                {
                   LegalDraftingFragment legalDraftingFragment = new LegalDraftingFragment();
                   legalDraftingFragment.show(getFragmentManager(),"");
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.user_menu);
        popupMenu.show();

    }







    }




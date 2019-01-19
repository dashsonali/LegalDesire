package com.example.user.legaldesire.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.legaldesire.LoginActivity;
import com.example.user.legaldesire.R;
import com.google.firebase.auth.FirebaseAuth;

public class LawyerProfileFragment extends Fragment {


    private  TextView name,email,areaOfPractice,phone,address,feeTxt,usersRated;
    SharedPreferences sharedPreferences;
    private RatingBar ratingBar;
    private ImageView user_menu;
    public LawyerProfileFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lawyer_profile, container, false);
        name = view.findViewById(R.id.nametxt);
        usersRated  = view.findViewById(R.id.usersRatedtext);
        areaOfPractice = view.findViewById(R.id.areaOfPracticeTxt);
        ratingBar = view.findViewById(R.id.ratingtxt);
        phone = view.findViewById(R.id.phoneTxt);
        email = view.findViewById(R.id.emailTxt);
        feeTxt = view.findViewById(R.id.feeTxt);
        user_menu = view.findViewById(R.id.user_menu);
        sharedPreferences = getContext().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name",null));
        ratingBar.setRating(Float.valueOf(sharedPreferences.getString("rating",null)));
        usersRated.setText(sharedPreferences.getString("usersRated",null)+" "+"user(s) have rated");
        areaOfPractice.setText(sharedPreferences.getString("areaOfPractice",null)+" "+"Lawyer");
        phone.setText(sharedPreferences.getString("contact",null));
        email.setText(sharedPreferences.getString("email",null));
        feeTxt.setText(sharedPreferences.getString("consultationFee",null));
        user_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_pop_up(view);
            }
        });
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

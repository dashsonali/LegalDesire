package com.example.user.legaldesire.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.legaldesire.R;

public class LawyerProfileFragment extends Fragment {


    private  TextView name,email,areaOfPractice,phone,address,penson,usersRated;
    SharedPreferences sharedPreferences;
    private RatingBar ratingBar;
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
        sharedPreferences = getContext().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name",null));
        ratingBar.setRating(Float.valueOf(sharedPreferences.getString("rating",null)));
        usersRated.setText(sharedPreferences.getString("usersRated",null)+" "+"user(s) have rated you");
        areaOfPractice.setText(sharedPreferences.getString("areaOfPractice",null));
        phone.setText(sharedPreferences.getString("contact",null));
        email.setText(sharedPreferences.getString("email",null));
        return view;
    }






}

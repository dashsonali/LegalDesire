package com.example.user.legaldesire.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.legaldesire.R;

public class LawyerProfileFragment extends Fragment {


    TextView name,email,areaOfPractice,mail,address,penson,usersRated,raing;

    public LawyerProfileFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lawyer_profile, container, false);

        return view;
    }






}

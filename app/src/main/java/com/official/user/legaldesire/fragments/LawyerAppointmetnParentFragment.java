package com.official.user.legaldesire.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.official.user.legaldesire.R;
import com.official.user.legaldesire.adapters.AppointmentParentPagerAdapter;


public class LawyerAppointmetnParentFragment extends Fragment {


    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AppointmentParentPagerAdapter appointmentParentPagerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lawyer_appointmetn_parent, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        appointmentParentPagerAdapter = new AppointmentParentPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(appointmentParentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Toast.makeText(getContext(),"IN HERE",Toast.LENGTH_SHORT).show();

        return view;
    }


}
